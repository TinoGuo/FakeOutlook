package me.tino.fakeoutlook.view

import android.annotation.TargetApi
import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.ArrayMap
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import me.tino.fakeoutlook.R
import me.tino.fakeoutlook.model.AgendaSubEvent
import me.tino.fakeoutlook.model.DayItem
import me.tino.fakeoutlook.model.WeatherInfo
import me.tino.fakeoutlook.model.WeatherSectionInfo
import me.tino.fakeoutlook.model.repository.WeatherRepository
import me.tino.fakeoutlook.view.adapter.AgendaAdapter
import me.tino.fakeoutlook.view.adapter.CalendarAdapter
import me.tino.fakeoutlook.view.adapter.WeekAdapter
import me.tino.fakeoutlook.view.decoration.AgendaItemDecoration
import me.tino.fakeoutlook.view.decoration.CalendarItemDecoration
import me.tino.fakeoutlook.view.snap.CalendarSnapHelper
import java.util.*
import kotlin.Comparator
import kotlin.math.roundToInt

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

    //temporary calendar used for date calculations
    private lateinit var tempDate: Calendar

    //the first day to show in calendar
    private val minDate = Calendar.getInstance()
    //the last day to show in calendar
    private val maxDate = Calendar.getInstance()
    private val currentDate = Calendar.getInstance()

    private var dateChangeListener: OnDateChangeListener? = null

    //now the top dayItem
    private var topDayItem: DayItem? = null

    //agenda event click listener
    private val onAgendaEventClick: (AgendaSubEvent) -> Unit = {
        // TODO: 9/25/18 create or update event
    }
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    //calendar item click listener
    private val onCalendarClick: (DayItem) -> Unit = { selectedDay ->
        //like outlook app, stop scroll behavior when click any calendar item
        agendaList.stopScroll()
        for (i in 0 until agendaAdapter.itemCount) {
            if (agendaAdapter.getItem(i).dayItem == selectedDay) {
                //ensure the select agenda is at top
                (agendaList.layoutManager as LinearLayoutManager).scrollToPositionWithOffset(i, 0)
                break
            }
        }
    }
    //listener when agendaList scroll
    private val agendaScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            //get the first visible position
            val firstVisiblePos =
                (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()

            val dayItem = agendaAdapter.getItem(firstVisiblePos).dayItem
            if (dayItem != topDayItem) {
                topDayItem = dayItem
                dateChangeListener?.onChange(dayItem.year, dayItem.month, dayItem.dayOfMonth)
            }

            //get the related calendar offset
            val calendarPosition = dayItem.offsetFromMin
            //update the selectedPosition
            calendarAdapter.updateSelected(calendarPosition)
            //if scroll out of the upper bound
            if ((calendarList.layoutManager as GridLayoutManager).findFirstVisibleItemPosition() > calendarPosition) {
                calendarList.scrollToPosition(calendarPosition)
            }
            //if scroll out of the lower bound
            if ((calendarList.layoutManager as GridLayoutManager).findLastVisibleItemPosition() < calendarPosition) {
                calendarList.scrollToPosition(calendarPosition)
            }
        }
    }

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

    override fun onDetachedFromWindow() {
        compositeDisposable.dispose()
        super.onDetachedFromWindow()
    }

    private fun initView(context: Context) {
        View.inflate(context, R.layout.widget_event_view, this)

        weekList = findViewById(R.id.weekList)
        weekList.adapter = WeekAdapter(WEEK)
        calendarList = findViewById(R.id.calendarView)
        agendaList = findViewById(R.id.agendaView)

        calendarList.addItemDecoration(CalendarItemDecoration(context))
        agendaList.addItemDecoration(AgendaItemDecoration())

        minDate.add(Calendar.MONTH, -2)
        maxDate.add(Calendar.YEAR, 1)

        fun submitCalculateResult(result: CalculateResult) {
            calendarAdapter = CalendarAdapter(result.dayItems, result.agendaOffset, onCalendarClick)
            calendarList.itemAnimator = null
            //helper class to align top item to the recyclerView
            CalendarSnapHelper().attachToRecyclerView(calendarList)
            calendarList.adapter = calendarAdapter

            agendaAdapter = AgendaAdapter(result.agendaSubEvents, onAgendaEventClick)
            agendaList.adapter = agendaAdapter
            agendaList.scrollToPosition(result.agendaOffset)

            agendaList.addOnScrollListener(agendaScrollListener)
        }

        fun submitWeather(result: Pair<WeatherInfo, Int>) {
            val event = agendaAdapter.getItem(result.second)
            val sectionInfoList = arrayListOf<WeatherSectionInfo>()
            val dataList = result.first.hourly.data
            var sum: Double
            var selectIcon = ""
            val iconCount = ArrayMap<String, Int>(8)
            for (i in 0 until dataList.size step 8) {
                //reset sum result
                sum = 0.0
                //clear map
                iconCount.clear()
                //every 8 hour a section
                for (j in 0..7) {
                    sum += dataList[i + j].temperature
                    val icon = dataList[i + j].icon
                    if (!iconCount.containsKey(icon)) {
                        iconCount[icon] = 0
                    }
                    //add visit count
                    iconCount[icon] = iconCount[icon]!! + 1
                }

                var max = Int.MIN_VALUE
                for ((k, v) in iconCount) {
                    //if count more than now max, replace count and icon
                    if (v > max) {
                        max = v
                        selectIcon = k
                    }
                }

                //calculate the average temperature in 8 hour
                val avgTemperature = (sum / 8).roundToInt()
                val sectionInfo = WeatherSectionInfo(selectIcon, avgTemperature)
                sectionInfoList.add(sectionInfo)
            }

            event.weatherSectionInfo = sectionInfoList
            agendaAdapter.notifyItemChanged(result.second)
        }

        val calDisposable = Single.create<CalculateResult> {
            it.onSuccess(calculateDays())
        }
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                submitCalculateResult(it)
            }
            .observeOn(Schedulers.io())
            .flatMapObservable { result ->
                val tempCalendar = Calendar.getInstance()
                //reset the time to the first mills in today
                tempCalendar.set(Calendar.HOUR_OF_DAY, 0)
                tempCalendar.set(Calendar.MINUTE, 0)
                tempCalendar.set(Calendar.SECOND, 0)
                tempCalendar.set(Calendar.MILLISECOND, 0)

                val today = tempCalendar.timeInMillis / 1000
                tempCalendar.add(Calendar.DATE, 1)
                val tomorrow = tempCalendar.timeInMillis / 1000

                return@flatMapObservable Observable.fromArray(today, tomorrow)
                    .flatMap { time ->
                        WeatherRepository.getWeatherInfo(time).map {
                            it to if (time == today) result.agendaOffset else result.agendaOffset + 1
                        }
                    }
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                submitWeather(it)
            }
        compositeDisposable.add(calDisposable)
    }

    /**
     * run in background thread to generate mock data
     * @return CalculateResult  mock data
     */
    private fun calculateDays(): CalculateResult {
        //the calendar day
        val dayItems = arrayListOf<DayItem>()
        //the agenda items
        val agendaItems = arrayListOf<AgendaSubEvent>()

        var dayItem: DayItem
        var selected: Boolean
        var calendarIndex = 0
        var nowCalendarIndex = -1
        var nowAgendaIndex = -1
        //for generate test data
        val random = Random()
        tempDate = minDate

        //when year and dayOfYear are both same means it is same day
        while (tempDate[Calendar.YEAR] != maxDate[Calendar.YEAR] ||
            tempDate[Calendar.DAY_OF_YEAR] != maxDate[Calendar.DAY_OF_YEAR]) {
            //check the tempDate equals to the current date
            selected = tempDate[Calendar.YEAR] == currentDate[Calendar.YEAR] &&
                    tempDate[Calendar.DAY_OF_YEAR] == currentDate[Calendar.DAY_OF_YEAR]
            if (selected) {
                nowCalendarIndex = calendarIndex
                //the position of next insert calendar equal to before insert list size
                nowAgendaIndex = agendaItems.size
            }

            //every item contains year, month, dayOfMonth and offsetFromMin
            dayItem = DayItem(
                tempDate[Calendar.DAY_OF_MONTH],
                tempDate[Calendar.MONTH],
                tempDate[Calendar.YEAR],
                calendarIndex
            )
            dayItems.add(dayItem)

            //just for generating fake data
            when (random.nextInt(20)) {
                in 0..1 -> {    //10% one day with two events
                    agendaItems.add(AgendaSubEvent.createRandomEvent(dayItem.copy(), random))
                    agendaItems.add(AgendaSubEvent.createRandomEvent(dayItem.copy(), random))
                }
                in 2..5 -> {    //20% one day with one event
                    agendaItems.add(AgendaSubEvent.createRandomEvent(dayItem.copy(), random))
                }
                else -> {       //70% one day without any event
                    agendaItems.add(AgendaSubEvent.createEmptyEvent(dayItem.copy()))
                }
            }

            //iterate to next day
            tempDate.add(Calendar.DATE, 1)
            calendarIndex++
        }
        return CalculateResult(dayItems, agendaItems, nowCalendarIndex, nowAgendaIndex)
    }

    fun setOnDateChangeListener(dateChangeListener: OnDateChangeListener) {
        this.dateChangeListener = dateChangeListener
    }

    interface OnDateChangeListener {
        /**
         * execute callback when the top agenda day change
         * @param year Int          the select year
         * @param month Int         the select month
         * @param dayOfMonth        Int the select dayOfMonth
         */
        fun onChange(year: Int, month: Int, dayOfMonth: Int)
    }

    /**
     * the init data wrapper class
     * @property dayItems List<DayItem>                 all the calendar day
     * @property agendaSubEvents List<AgendaSubEvent>   all the agendaSubEvent
     * @property calendarOffset Int                     the offset from first day to now
     * @property agendaOffset Int                       the offset from first event to today's event
     * @constructor
     */
    private data class CalculateResult(
        val dayItems: List<DayItem>,
        val agendaSubEvents: List<AgendaSubEvent>,
        val calendarOffset: Int,
        val agendaOffset: Int
    )

    private companion object {
        val WEEK = listOf("S", "W", "T", "W", "T", "F", "S")
    }
}