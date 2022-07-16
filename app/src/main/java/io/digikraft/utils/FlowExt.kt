package io.digikraft.utils

import kotlinx.coroutines.flow.MutableStateFlow

fun <T> MutableStateFlow<T>.updateValue(updateFn: T.() -> T): T {
    val updatedValue = updateFn(this.value)
    this.value = updatedValue
    return updatedValue
}