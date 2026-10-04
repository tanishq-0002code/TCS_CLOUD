package com.example.telegramcloudgallery.data.telegram

import org.drinkless.tdlib.TdApi

/**
 * Extracts the metadata tags from a Telegram message caption.
 *
 * ### Why captions matter here
 * The spec's dual mapping strategy has two halves; this class implements the
 * caption half:
 *
 * ```
 * beach.mp4
 * #Folder_Trip2026 #Family #Vacation
 * ```
 *
 * When a media message is indexed, the hashtags in its caption are turned into
 * real `MediaTagEntity` rows, and any hashtag of the form
 * `#Folder_<name>` is additionally used to resolve the matching **virtual
 * folder**. The caption therefore becomes durable, human-editable metadata that
 * survives re-syncing, unlike a purely local index that is lost on reinstall.
 *
 * Parsing prefers TDLib's own [TdApi.TextEntity] list (exact offsets, respects
 * UTF-16 code units) and only falls back to a regex when entities are absent —
 * which happens for captions typed outside Telegram.
 */
object CaptionTagParser {

    /**
     * Reserved prefix that promotes a hashtag into a *folder* reference,
     * e.g. `#Folder_Trip2026`.
     */
    const val FOLDER_TAG_PREFIX = "Folder_"

    /** Telegram's rule for a legal hashtag: letters, digits, underscore. */
    private val HASHTAG_REGEX = Regex("#([\\p{L}\\p{N}_]{1,64})")

    /** Tag names are normalised to lower-case so `#Vacation` == `#vacation`. */
    private val NORMALISE_REGEX = Regex("[^\\p{L}\\p{N}_]")

    /**
     * @param caption raw caption text, or `null`.
     * @param entities TDLib text entities belonging to the caption, if known.
     * @return normalised tag names **without** the leading `#`, de-duplicated,
     *         preserving first-seen order.
     */
    fun extractTags(caption: String?, entities: Array<TdApi.TextEntity>? = null): List<String> {
        if (caption.isNullOrBlank()) return emptyList()

        val raw = if (!entities.isNullOrEmpty()) {
            entitiesFromEntities(caption, entities)
        } else {
            HASHTAG_REGEX.findAll(caption).map { it.groupValues[1] }.toList()
        }

        return raw.asSequence()
            .map(::normalise)
            .filter { it.isNotEmpty() }
            .distinct()
            .toList()
    }

    /** Same as [extractTags] but keeps the `#` prefix, for display. */
    fun extractDisplayTags(
        caption: String?,
        entities: Array<TdApi.TextEntity>? = null
    ): List<String> = extractTags(caption, entities).map { "#$it" }

    /**
     * Folder names referenced by `#Folder_<name>` hashtags.
     *
     * `Folder_` itself is dropped, and the remaining text is de-duplicated.
     */
    fun extractFolderNames(caption: String?, entities: Array<TdApi.TextEntity>? = null): List<String> =
        extractTags(caption, entities)
            .filter { it.startsWith(FOLDER_TAG_PREFIX, ignoreCase = true) }
            .map { it.substring(FOLDER_TAG_PREFIX.length) }
            .filter { it.isNotBlank() }
            .distinct()

    /**
     * Splits hashtags into *tags* (kept) and *folder references* (returned
     * separately), so the two can be persisted into different tables.
     */
    fun partition(caption: String?, entities: Array<TdApi.TextEntity>? = null): PartitionedTags {
        val all = extractTags(caption, entities)
        return PartitionedTags(
            tags = all.filterNot { it.startsWith(FOLDER_TAG_PREFIX, ignoreCase = true) },
            folderNames = all
                .filter { it.startsWith(FOLDER_TAG_PREFIX, ignoreCase = true) }
                .map { it.substring(FOLDER_TAG_PREFIX.length) }
                .filter { it.isNotBlank() }
        )
    }

    /** Strips `#`, lower-cases, and removes characters Telegram would not allow. */
    fun normalise(raw: String): String =
        NORMALISE_REGEX.replace(raw.trim().removePrefix("#")).lowercase()

    /** Reads the exact substrings TDLib marked as hashtags. */
    private fun entitiesFromEntities(
        caption: String,
        entities: Array<TdApi.TextEntity>
    ): List<String> = entities
        .asSequence()
        .filter { it.type is TdApi.TextEntityTypeHashtag }
        .mapNotNull { entity ->
            runCatching {
                caption.substring(entity.offset, entity.offset + entity.length)
                    .removePrefix("#")
            }.getOrNull()
        }
        .toList()

    /** Result of [partition]. */
    data class PartitionedTags(
        /** Free-form tags to create/attach in `media_tags`. */
        val tags: List<String>,
        /** Virtual folder names to resolve via `#Folder_<name>` tags. */
        val folderNames: List<String>
    ) {
        val isEmpty: Boolean get() = tags.isEmpty() && folderNames.isEmpty()
    }
}