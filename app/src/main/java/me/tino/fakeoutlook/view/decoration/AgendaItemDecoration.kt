package me.tino.fakeoutlook.view.decoration

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import me.tino.fakeoutlook.ext.children
import me.tino.fakeoutlook.ext.dp2px
import me.tino.fakeoutlook.ext.sp2px
import me.tino.fakeoutlook.model.DayItem
import me.tino.fakeoutlook.view.adapter.AgendaAdapter
import java.util.*

/**
 * archive sticky header for AgendaViewList
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 September 22, 21:33.
 */
class AgendaItemDecoration : RecyclerView.ItemDecoration() {

    private val displayCalendar = Calendar.getInstance()
    private val textPaint = Paint()
    private val dividerPaint = Paint()
    private val headerStrokePaint = Paint()
    private val headerBgPaint = Paint()
    private val headerHeight = 24.dp2px<Int>()
    private val paddingLeft = 12.dp2px<Float>()
    private val dividerLineHeight = 0.5.dp2px<Int>()
    //to measure the text bound
    private val bound = Rect()

    init {
        textPaint.color = Color.GRAY
        textPaint.isAntiAlias = true
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.textSize = 14.sp2px()
        textPaint.isFakeBoldText = true

        dividerPaint.color = Color.LTGRAY

        headerBgPaint.color = Color.WHITE
        headerBgPaint.style = Paint.Style.FILL

        headerStrokePaint.color = Color.LTGRAY
        headerStrokePaint.style = Paint.Style.STROKE
        headerStrokePaint.strokeWidth = 0.5.dp2px()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val adapter = parent.adapter as? AgendaAdapter ?: return
        //the current day
        val dayItem = adapter.getItem(position).dayItem

        if (position == 0) {
            outRect.top = headerHeight
            //save the text to the tag of view
            view.tag = getCalendarDisplayName(dayItem)
            return
        }
        //override the equal operator, so we can just use equal operator
        val previousDayItem = adapter.getItem(position - 1).dayItem
        if (dayItem != previousDayItem) {
            outRect.top = headerHeight
            //save the text to the tag of view
            view.tag = getCalendarDisplayName(dayItem)
        }
    }

    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        parent.children.forEach { child ->
            //if the tag is not null, it means there is the header of the day
            //otherwise, we should draw a divider between the event of same day
            child.tag?.takeIf { it is String }?.run {
                drawCalendarText(c, child, this as String)
            } ?: c.drawRect(
                paddingLeft,
                (child.top - dividerLineHeight).toFloat(),
                parent.right.toFloat(),
                child.top.toFloat(),
                dividerPaint
            )
        }
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val adapter = parent.adapter as? AgendaAdapter ?: return
        //find the first item
        val position = (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        //get the related position view
        val child = parent.findViewHolderForAdapterPosition(position).itemView
        //the bottom of the header
        var bottom = headerHeight
        //we should consider the situation that the previous float header right next to the normal header
        if (position < adapter.itemCount && parent.findViewHolderForAdapterPosition(position + 1).itemView.tag != null) {
            bottom = Math.min(bottom, child.bottom)
        }

        //if the tag is not null, it means it is the top event of the day
        if (child.tag != null) {
            drawOverText(c, child.tag.toString(), bottom, child)
        }
    }

    /**
     * draw the float text with white rect
     * @param c Canvas
     * @param text String
     * @param bottom Int
     * @param child View
     */
    private fun drawOverText(c: Canvas, text: String, bottom: Int, child: View) {
        val upperText = child.tag.toString().toUpperCase()
        textPaint.getTextBounds(upperText, 0, text.length, bound)
        //draw the background in case of the overlay
        c.drawRect(0f, 0f, child.right.toFloat(), bottom.toFloat(), headerBgPaint)
        c.drawRect(0f, 0f, child.right.toFloat(), bottom.toFloat(), headerStrokePaint)

        //draw the center vertical text
        c.drawText(
            upperText,
            bound.width() / 2f + paddingLeft,
            bottom - headerHeight / 2f + bound.height() / 2f,
            textPaint
        )
    }

    /**
     * draw the normal header
     * @param c Canvas
     * @param child View
     * @param text String
     */
    private fun drawCalendarText(c: Canvas, child: View, text: String) {
        c.drawRect(
            0f,
            (child.top - headerHeight).toFloat(),
            child.right.toFloat(),
            child.top.toFloat(),
            headerBgPaint
        )
        c.drawRect(
            0f,
            (child.top - headerHeight).toFloat(),
            child.right.toFloat(),
            child.top.toFloat(),
            headerStrokePaint
        )

        //All the text is upper case
        val upperText = text.toUpperCase()
        //measure the text bound for the late layout
        textPaint.getTextBounds(upperText, 0, upperText.length, bound)

        c.drawText(
            upperText,
            bound.width() / 2f + paddingLeft,
            child.top - headerHeight / 2f + bound.height() / 2f,
            textPaint
        )
    }

    private fun getCalendarDisplayName(dayItem: DayItem): String {
        displayCalendar.set(dayItem.year, dayItem.month, dayItem.dayOfMonth)
        return "${displayCalendar.getDisplayName(
            Calendar.DAY_OF_WEEK,
            Calendar.LONG,
            Locale.getDefault()
        )}, ${dayItem.dayOfMonth} ${displayCalendar.getDisplayName(
            Calendar.MONTH,
            Calendar.LONG,
            Locale.getDefault()
        )} "
    }
}