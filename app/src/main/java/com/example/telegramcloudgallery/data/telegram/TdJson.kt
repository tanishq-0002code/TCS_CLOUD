package com.example.telegramcloudgallery.data.telegram

import android.util.Log
import org.drinkless.tdlib.TdApi
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

/**
 * JSON helpers around TDLib objects.
 *
 * The bundled `org.drinkless.tdlib` binding overrides `TdApi.Object.toString()`
 * as a **native** method that returns the object's JSON representation, so
 * serialising is free — no hand written TDLib↔JSON converter is required.
 * This object only adds tolerant parsing and a couple of convenience readers.
 */
object TdJson {

    private const val TAG = "TdJson"

    /** Serialises any TDLib object to its JSON form. */
    fun toJson(value: TdApi.Object?): String = value?.toString() ?: "null"

    /** Never-throwing variant used on logging paths. */
    fun toJsonOrNull(value: TdApi.Object?): String? = try {
        toJson(value)
    } catch (t: Throwable) {
        Log.w(TAG, "toString() threw for ${value?.javaClass?.simpleName}", t)
        null
    }

    fun parseObject(raw: String): JSONObject = JSONObject(raw)

    fun parseObjectOrNull(raw: String?): JSONObject? = try {
        if (raw.isNullOrBlank()) null else JSONObject(raw)
    } catch (e: JSONException) {
        Log.w(TAG, "Not a JSON object: ${raw?.take(120)}", e)
        null
    }

    fun parseArray(raw: String?): JSONArray? = try {
        if (raw.isNullOrBlank()) null else JSONArray(raw)
    } catch (e: JSONException) {
        Log.w(TAG, "Not a JSON array: ${raw?.take(120)}", e)
        null
    }

    // ----------------------------------------------------- option value readers

    fun OptionValue.asString(): String? = when (this) {
        is TdApi.OptionValueString -> value
        is TdApi.OptionValueInteger -> value.toString()
        is TdApi.OptionValueBoolean -> value.toString()
        else -> null
    }

    fun OptionValue.asBooleanOrNull(): Boolean? = (this as? TdApi.OptionValueBoolean)?.value

    fun OptionValue.asIntOrNull(): Int? = (this as? TdApi.OptionValueInteger)?.value?.toInt()

    // ------------------------------------------------------------ error mapping

    /** Human readable message for a `TdApi.Error`, preferring TDLib's own text. */
    fun TdApi.Error.describe(): String =
        message?.takeIf { it.isNotBlank() } ?: describeErrorCode(code)

    /**
     * Best-effort English explanation for the TDLib error codes the gallery can
     * realistically hit. Anything unknown falls back to the numeric code.
     */
    fun describeErrorCode(code: Int): String = when (code) {
        400 -> "Bad request: one of the entered values is invalid"
        401 -> "Not authorized"
        403 -> "Forbidden: the account lacks permission for this action"
        404 -> "Not found"
        420 -> "Flood wait: too many requests, retry later"
        429 -> "Rate limited by Telegram, retry later"
        440 -> "The code has expired"
        442 -> "Session expired — please sign in again"
        443 -> "Too many requests, retry later"
        500 -> "Internal TDLib error"
        else -> "Telegram error $code"
    }
}