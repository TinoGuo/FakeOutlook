package me.tino.fakeoutlook.ext

import android.content.res.Resources
import android.util.TypedValue

/**
 * the extension for displayMetric calculating
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 September 22, 21:56.
 */
@Suppress("IMPLICIT_CAST_TO_ANY")
inline fun <reified T : Number> Number.dp2px(): T {
    if (this is Byte) {
        throw IllegalArgumentException("byte is not support type!!!")
    }
    val value = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )
    return when (T::class) {
        Int::class -> value.toInt()
        Float::class -> value
        Double::class -> value.toDouble()
        Long::class -> value.toLong()
        Short::class -> value.toShort()
        else -> throw TypeCastException("${T::class} must be subclass of the Number!!!")
    } as T
}

@Suppress("IMPLICIT_CAST_TO_ANY")
inline fun <reified T : Number> Number.sp2px(): T {
    if (this is Byte) {
        throw IllegalArgumentException("byte is not support type!!!")
    }
    val value = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )
    return when (T::class) {
        Int::class -> value.toInt()
        Float::class -> value
        Double::class -> value.toDouble()
        Long::class -> value.toLong()
        Short::class -> value.toShort()
        else -> throw TypeCastException("${T::class} must be subclass of the Number!!!")
    } as T
}