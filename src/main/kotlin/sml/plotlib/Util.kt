package sml.plotlib

fun <T> MutableList<T>.replaceAllWith(newValues: List<T>): MutableList<T> {
    require(size == newValues.size)
    for (i in indices) this[i] = newValues[i]
    return this
}
