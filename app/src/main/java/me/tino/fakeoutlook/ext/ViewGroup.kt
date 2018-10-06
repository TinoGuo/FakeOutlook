package me.tino.fakeoutlook.ext

import android.view.View
import android.view.ViewGroup

/**
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 October 06, 21:52.
 */
/** Returns a [MutableIterator] over the views in this view group. */
operator fun ViewGroup.iterator() = object : MutableIterator<View> {
    private var index = 0
    override fun hasNext() = index < childCount
    override fun next() = getChildAt(index++) ?: throw IndexOutOfBoundsException()
    override fun remove() = removeViewAt(--index)
}

/** Returns a [Sequence] over the child views in this view group. */
val ViewGroup.children: Sequence<View>
    get() = object : Sequence<View> {
        override fun iterator() = this@children.iterator()
    }