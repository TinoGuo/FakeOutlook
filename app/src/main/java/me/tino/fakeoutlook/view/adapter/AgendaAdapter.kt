package me.tino.fakeoutlook.view.adapter

import android.annotation.SuppressLint
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_agenda.view.*
import me.tino.fakeoutlook.R
import me.tino.fakeoutlook.annotation.EventType
import me.tino.fakeoutlook.model.AgendaSubEvent
import me.tino.fakeoutlook.view.WeatherWidget

/**
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 September 22, 21:11.
 */
class AgendaAdapter(
    agendaEventList: List<AgendaSubEvent>,
    private val agendaClick: (AgendaSubEvent) -> Unit
) :
    BaseDataBoundAdapter<AgendaSubEvent, RecyclerView.ViewHolder>(object :
        DiffUtil.ItemCallback<AgendaSubEvent>() {
        override fun areItemsTheSame(oldItem: AgendaSubEvent, newItem: AgendaSubEvent): Boolean {
            return oldItem.dayItem.year == newItem.dayItem.year &&
                    oldItem.dayItem.month == newItem.dayItem.month &&
                    oldItem.dayItem.dayOfMonth == newItem.dayItem.dayOfMonth
        }

        override fun areContentsTheSame(
            oldItem: AgendaSubEvent,
            newItem: AgendaSubEvent
        ): Boolean {
            return oldItem.anyEvent == newItem.anyEvent && oldItem.title == newItem.title &&
                    oldItem.hour == newItem.hour && oldItem.min == newItem.min &&
                    oldItem.type == newItem.type && oldItem.location == newItem.location
        }
    }) {

    init {
        submitList(agendaEventList)
    }

    override fun getItemViewType(position: Int): Int {
        return when {
            getItem(position).weatherSectionInfo?.isNotEmpty() == true -> FORECAST
            getItem(position).anyEvent -> EVENTS
            else -> EMPTY
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            EVENTS -> {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.item_agenda, parent, false)
                EventViewHolder(view)
            }
            EMPTY -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_empty_agenda, parent, false)
                EmptyViewHolder(view)
            }
            FORECAST -> {
                val view = WeatherWidget(parent.context)
                view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                ForecastViewHolder(view)
            }
            else -> throw IllegalArgumentException("no related viewType:$viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is EmptyViewHolder -> {
                //no need to set variable
            }
            is EventViewHolder -> {
                holder.bind(getItem(position))
            }
            is ForecastViewHolder -> {
                holder.bind(getItem(position))
            }
            else -> throw IllegalArgumentException("no related viewHolder:$holder")
        }
    }

    private companion object {
        private const val EMPTY = 0
        private const val EVENTS = 1
        private const val FORECAST = 2
    }

    private inner class EventViewHolder(view: View) : RecyclerView.ViewHolder(view),
        ViewBind<AgendaSubEvent> {
        private lateinit var agendaSubEvent: AgendaSubEvent

        @SuppressLint("SetTextI18n")
        override fun bind(value: AgendaSubEvent) {
            this.agendaSubEvent = value

            itemView.startTime.text = String.format("%02d:%02d", value.hour, value.min)
            itemView.lastTime.text = "${value.lastTime}h"
            when (value.type) {
                EventType.COFFEE -> itemView.eventType.setImageResource(R.drawable.ic_coffee)
                EventType.DINNER -> itemView.eventType.setImageResource(R.drawable.ic_restaurant)
            }
            itemView.eventTitle.text = value.title
            if (value.location == null) {
                itemView.eventLocation.visibility = View.GONE
            } else {
                itemView.eventLocation.visibility = View.VISIBLE
                itemView.eventLocation.text = value.location
            }

            itemView.setOnClickListener {
                agendaClick(agendaSubEvent)
            }
        }
    }

    private class ForecastViewHolder(view: View) : RecyclerView.ViewHolder(view),
        ViewBind<AgendaSubEvent> {
        override fun bind(value: AgendaSubEvent) {
            value.weatherSectionInfo
                ?.takeIf { it.isNotEmpty() }
                ?.let {
                    (itemView as WeatherWidget).updaterWeatherInfo(it)
                }
        }
    }

    private class EmptyViewHolder(view: View) : RecyclerView.ViewHolder(view)
}