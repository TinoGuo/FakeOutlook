package me.tino.fakeoutlook.view.decoration

import android.content.Context
import android.graphics.Canvas
import android.graphics.Rect
import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import me.tino.fakeoutlook.R

/**
 * the divider between weeks
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 September 22, 19:18.
 */
class CalendarItemDecoration(context: Context): RecyclerView.ItemDecoration() {

    private val _divider = ContextCompat.getDrawable(context,
        R.drawable.calendar_divider
    )!!

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        outRect.set(0, 0, _divider.intrinsicWidth, _divider.intrinsicHeight)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val childCount = parent.childCount
        for (i in 0 until childCount) {
            val child = parent.getChildAt(i)
            val params = child.layoutParams as RecyclerView.LayoutParams
            val left = child.left - params.leftMargin
            val top = child.bottom + params.bottomMargin
            val right = child.right + params.rightMargin
            val bottom = top + _divider.minimumHeight
            _divider.setBounds(left, top, right, bottom)
            _divider.draw(c)
        }
    }
}