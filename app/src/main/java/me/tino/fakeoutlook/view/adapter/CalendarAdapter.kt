package me.tino.fakeoutlook.view.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_calendar.view.*
import me.tino.fakeoutlook.R
import me.tino.fakeoutlook.model.DayItem

/**
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 September 22, 18:21.
 */
class CalendarAdapter(private var selectedPosition: Int) :
    BaseDataBoundAdapter<DayItem, CalendarAdapter.CalendarViewHolder>(object : DiffUtil.ItemCallback<DayItem>() {
        override fun areItemsTheSame(oldItem: DayItem, newItem: DayItem): Boolean {
            return oldItem.month == newItem.month && oldItem.offset == newItem.offset
        }

        override fun areContentsTheSame(oldItem: DayItem, newItem: DayItem): Boolean {
            return oldItem.offset == newItem.offset
        }
    }) {

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_calendar, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: CalendarViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        if (payloads.isNotEmpty()) {
            when (payloads.component1()) {
                is Boolean -> holder.bindPart(payloads.component1() as Boolean)
            }
        } else {
            super.onBindViewHolder(holder, position, payloads)
        }
    }

    inner class CalendarViewHolder(view: View): RecyclerView.ViewHolder(view), ViewBind<DayItem> {
        private lateinit var dayItem: DayItem

        fun bindPart(changeValue: Boolean) {
            itemView.day.isSelected = changeValue
            itemView.dayBackground.isSelected = changeValue
        }

        override fun bind(value: DayItem) {
            this.dayItem = value

            itemView.day.text = value.offset.toString()
            itemView.month.text = value.month
            itemView.dayBackground.isSelected = value.selected
            itemView.day.isSelected = value.selected

            itemView.setOnClickListener {
                if (selectedPosition == adapterPosition) {
                    //no need to update same select position
                    return@setOnClickListener
                }
                notifyItemChanged(selectedPosition, false)
                dayItem.selected = true
                itemView.day.isSelected = true
                itemView.dayBackground.isSelected = true
                selectedPosition = adapterPosition
            }
        }
    }

}