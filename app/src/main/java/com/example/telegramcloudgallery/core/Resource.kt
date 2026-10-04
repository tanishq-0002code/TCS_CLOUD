package com.example.telegramcloudgallery.core

/**
 * Minimal, explicit result wrapper used by every repository boundary so the
 * ViewModel layer never has to catch raw [Throwable].
 */
sealed interface Resource<out T> {

    data object Loading : Resource<Nothing>

    data class Success<T>(val data: T) : Resource<T>

    data class Failure(
        val message: String,
        val cause: Throwable? = null,
        val code: Int = UNKNOWN_CODE
    ) : Resource<Nothing> {

        companion object {
            const val UNKNOWN_CODE = -1
        }
    }

    val isLoading: Boolean get() = this is Loading

    fun dataOrNull(): T? = (this as? Success)?.data

    inline fun <R> map(transform: (T) -> R): Resource<R> = when (this) {
        is Loading -> Loading
        is Failure -> this
        is Success -> Success(transform(data))
    }

    inline fun onSuccess(action: (T) -> Unit): Resource<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onFailure(action: (Failure) -> Unit): Resource<T> {
        if (this is Failure) action(this)
        return this
    }
}