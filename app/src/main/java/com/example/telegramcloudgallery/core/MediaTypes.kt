package com.example.telegramcloudgallery.core

/**
 * Every kind of media the gallery is able to index.
 *
 * Stored in Room's `media_items.mediaType` column — Room's built-in enum
 * converter persists it as the enum's `name`, so the column stays readable in
 * raw SQL / `adb shell sqlite3`.
 */
enum class MediaKind {
    PHOTO,
    VIDEO,
    ANIMATION,
    AUDIO,
    DOCUMENT,
    UNKNOWN
}

/**
 * Lifecycle of a media item with respect to the *cloud* (Telegram) side.
 *
 * This is what the gallery grid renders as an "upload status indicator".
 */
enum class UploadState {
    /** Purely local item, nothing scheduled for Telegram. */
    NONE,

    /** Detected by the sync engine, waiting for its turn. */
    PENDING,

    /** Handed to TDLib (`PreliminaryUploadFile`), transfer in progress. */
    UPLOADING,

    /** Fully uploaded and present in the mapped Telegram chat / folder. */
    UPLOADED,

    /** Upload failed; [com.example.telegramcloudgallery.data.db.entity.MediaItemEntity.uploadError] explains why. */
    FAILED
}

/** Thumbnail / video quality selector shared by the gallery and reel feed. */
enum class PreviewQuality {
    THUMBNAIL,
    SMALL,
    MEDIUM,
    LARGE,
    FULL
}