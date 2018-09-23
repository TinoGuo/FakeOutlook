package me.tino.fakeoutlook.view

import android.annotation.TargetApi
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import me.tino.fakeoutlook.R
import me.tino.fakeoutlook.view.adapter.AgendaAdapter
import me.tino.fakeoutlook.view.adapter.CalendarAdapter
import me.tino.fakeoutlook.view.adapter.WeekAdapter

/**
 * the combine view of the week, calendar and agenda
 *
 * -----------WeekList-----------
 * ||||||||||||||||||||||||||||||
 * |||||||||CalendarList|||||||||
 * ||||||||||||||||||||||||||||||
 * ******************************
 * **********AgendaList**********
 * ******************************
 *
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 September 23, 20:32.
 */
class EventView : LinearLayout {
    private lateinit var calendarList: RecyclerView
    private lateinit var agendaList: RecyclerView
    private lateinit var weekList: RecyclerView
    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var agendaAdapter: AgendaAdapter

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    @TargetApi(21)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        initView(context)
    }

    private fun initView(context: Context) {
        View.inflate(context, R.layout.widget_event_view, this)

        weekList = findViewById(R.id.weekList)
        weekList.adapter = WeekAdapter(WEEK)
        calendarList = findViewById(R.id.calendarView)
        agendaList = findViewById(R.id.agendaView)
    }

    private companion object {
        val WEEK = arrayListOf("S", "W", "T", "W", "T", "F", "S")
    }
}