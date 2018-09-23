package me.tino.fakeoutlook.view.snap

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SnapHelper
import android.view.View

/**
 * the listHelper class to fix the position of the calendarView
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 September 22, 17:34.
 */
class CalendarSnapHelper: SnapHelper() {
    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray? {
        TODO()
    }

    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager?,
        velocityX: Int,
        velocityY: Int
    ): Int {
        TODO()
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager?): View? {
        TODO()
    }
}