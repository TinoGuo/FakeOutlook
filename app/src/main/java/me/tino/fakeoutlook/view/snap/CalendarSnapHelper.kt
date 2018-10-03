package me.tino.fakeoutlook.view.snap

import android.support.v7.widget.OrientationHelper
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SnapHelper
import android.view.View

/**
 * the listHelper class to fix the position of the calendarView
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 September 22, 17:34.
 */
@Suppress("MemberVisibilityCanBePrivate")
class CalendarSnapHelper(
    private val spanCount: Int = 7,
    var rowCount: Int = 2
) : SnapHelper() {
    private var helper: OrientationHelper? = null

    private val pageCount: Int
        get() = spanCount * rowCount

    /**
     * create [OrientationHelper] if not create before or the layoutManager changed
     * @param layoutManager         RecyclerView.LayoutManager  RecyclerView.LayoutManager  The [RecyclerView.LayoutManager] associated with the attached
     *                      [RecyclerView].
     * @return OrientationHelper    The relevant [OrientationHelper] for the attached [RecyclerView].
     */
    private fun getOrientationHelper(layoutManager: RecyclerView.LayoutManager): OrientationHelper {
        if (helper == null || helper!!.layoutManager != layoutManager) {
            return OrientationHelper.createVerticalHelper(layoutManager)
        }
        return helper!!
    }

    override fun calculateDistanceToFinalSnap(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View
    ): IntArray {
        val out = IntArray(2)
        //in calendar widget, only scroll vertically
        out[0] = 0
        if (helper == null) {
            helper = OrientationHelper.createVerticalHelper(layoutManager)
        }
        out[1] = getDistance(layoutManager, targetView, getOrientationHelper(layoutManager))
        return out
    }

    /**
     * calculate the distance between the after scrolling and [targetView]
     * @param layoutManager RecyclerView.LayoutManager  RecyclerView.LayoutManager  The [RecyclerView.LayoutManager] associated with the attached
     *                      [RecyclerView].
     * @param targetView View                           to align view
     * @param helper OrientationHelper                  The relevant [OrientationHelper] for the attached [RecyclerView].
     * @return Int                                      the distance in pixels
     */
    private fun getDistance(
        layoutManager: RecyclerView.LayoutManager,
        targetView: View,
        helper: OrientationHelper
    ): Int {
        val totalHeight = layoutManager.height
        val rowHeight = totalHeight / rowCount
        val position = layoutManager.getPosition(targetView)
        val pageIndex = position / pageCount
        val currentPageStart = pageIndex * pageCount
        val distance = ((position - currentPageStart) / spanCount) * rowHeight

        val childStart = helper.getDecoratedStart(targetView)
        return childStart - distance
    }

    override fun findTargetSnapPosition(
        layoutManager: RecyclerView.LayoutManager,
        velocityX: Int,
        velocityY: Int
    ): Int {
        val itemCount = layoutManager.itemCount
        if (itemCount == 0) {
            return RecyclerView.NO_POSITION
        }

        val startMostChildView = findStartView(layoutManager, getOrientationHelper(layoutManager))
                ?: return RecyclerView.NO_POSITION

        val centerPosition = layoutManager.getPosition(startMostChildView)
        if (centerPosition == RecyclerView.NO_POSITION) {
            return RecyclerView.NO_POSITION
        }

        val forwardDirection = velocityY > 0

        val pageIndex = centerPosition / pageCount
        val currentPageStart = pageIndex * pageCount

        return if (forwardDirection) currentPageStart + pageCount else currentPageStart + pageIndex - 1
    }

    /**
     * Return the top align view
     * @param layoutManager RecyclerView.LayoutManager  RecyclerView.LayoutManager  The [RecyclerView.LayoutManager] associated with the attached
     *                      [RecyclerView].
     * @param helper OrientationHelper  The relevant [OrientationHelper] for the attached [RecyclerView].
     * @return View?
     */
    private fun findStartView(
        layoutManager: RecyclerView.LayoutManager,
        helper: OrientationHelper
    ): View? {
        val childCount = layoutManager.childCount
        if (childCount == 0) {
            return null
        }

        var closestChild: View? = null
        var absClosest = Int.MAX_VALUE

        for (i in 0 until childCount) {
            val child = layoutManager.getChildAt(i)
            val childStart = helper.getDecoratedStart(child)

            /** if child center is closer than previous closest, set it as closest  **/
            if (childStart < absClosest) {
                absClosest = childStart
                closestChild = child
            }
        }
        return closestChild
    }

    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        return findCenterView(layoutManager, getOrientationHelper(layoutManager))
    }

    /**
     * Return the child view that is currently closest to the center of this parent.
     * @param layoutManager RecyclerView.LayoutManager  The [RecyclerView.LayoutManager] associated with the attached
     *                      [RecyclerView].
     * @param helper OrientationHelper  The relevant [OrientationHelper] for the attached [RecyclerView].
     * @return View?
     */
    private fun findCenterView(
        layoutManager: RecyclerView.LayoutManager,
        helper: OrientationHelper
    ): View? {
        val childCount = layoutManager.childCount
        if (childCount == 0) {
            return null
        }

        var closestChild: View? = null
        val center: Int = if (!layoutManager.clipToPadding) {
            helper.end / 2
        } else {
            helper.startAfterPadding + helper.totalSpace / 2
        }

        var absClosest = Int.MAX_VALUE

        for (i in 0 until childCount) {
            val child = layoutManager.getChildAt(i)
            val childCenter =
                helper.getDecoratedStart(child) + (helper.getDecoratedMeasurement(child) / 2)
            val absDistance = Math.abs(childCenter - center)

            /** if child center is closer than previous closest, set it as closest  **/
            if (absDistance < absClosest) {
                absClosest = absDistance
                closestChild = child
            }
        }
        return closestChild
    }
}