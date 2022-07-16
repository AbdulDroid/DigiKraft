package io.digikraft.utils

open class Event<out T>(private val content: T) {

    var hasBeenConsumed: Boolean = false
        private set // Allow external read but not write

    /**
     * Returns the content and prevents its use again.
     */
    fun getContentIfNotConsumed(): T? {
        return if (hasBeenConsumed) {
            null
        } else {
            hasBeenConsumed = true
            content
        }
    }

    /**
     *  Return the content, even if it has already been consumed.
     */
    fun peekContent(): T = content
}