package me.tino.fakeoutlook.view.decoration

import android.graphics.Canvas
import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View

/**
 * archive sticky header for AgendaViewList
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 September 22, 21:33.
 */
class AgendaItemDecoration: RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
    }

    private fun getGroupName(index: Int): String {
        TODO()
    }

    private fun isFirstGroup(index: Int): Boolean {
        TODO()
    }
}