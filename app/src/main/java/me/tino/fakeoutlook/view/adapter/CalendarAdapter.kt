package me.tino.fakeoutlook.view.adapter

import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_calendar.view.*
import me.tino.fakeoutlook.R
import me.tino.fakeoutlook.model.DayItem
import java.util.*

/**
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 September 22, 18:21.
 */
class CalendarAdapter(
    calendarList: List<DayItem>,
    private var selectedPosition: Int,
    private val calendarSelectedBlock: (DayItem) -> Unit) :
    BaseDataBoundAdapter<DayItem, CalendarAdapter.CalendarViewHolder>(object : DiffUtil.ItemCallback<DayItem>() {
        override fun areItemsTheSame(oldItem: DayItem, newItem: DayItem): Boolean {
            return oldItem.month == newItem.month &&
                    oldItem.dayOfMonth == newItem.dayOfMonth &&
                    oldItem.year == newItem.year
        }

        override fun areContentsTheSame(oldItem: DayItem, newItem: DayItem): Boolean {
            return oldItem.month == newItem.month &&
                    oldItem.dayOfMonth == newItem.dayOfMonth &&
                    oldItem.year == newItem.year
        }
    }) {
    private val calculateDisplayDate = Calendar.getInstance()

    init {
        submitList(calendarList)
    }

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

    fun updateSelected(selectedPosition: Int) {
        if (selectedPosition == this.selectedPosition) {
            //if selectedPosition equal to the parameter, wo should ignore in case of updateView
            return
        }
        notifyItemChanged(this.selectedPosition, false)
        this.selectedPosition = selectedPosition
        notifyItemChanged(this.selectedPosition, true)
    }

    private fun getMonthDisplayName(month: Int): String {
        check(month in 0..11) {
            "month must >=0 and < 12"
        }
        calculateDisplayDate.set(Calendar.MONTH, month)
        return calculateDisplayDate.getDisplayName(Calendar.MONTH, Calendar.SHORT, Locale.getDefault())
    }

    inner class CalendarViewHolder(view: View): RecyclerView.ViewHolder(view), ViewBind<DayItem> {
        private lateinit var dayItem: DayItem

        fun bindPart(changeValue: Boolean) {
            itemView.day.isSelected = changeValue
            itemView.dayBackground.isSelected = changeValue
            if (dayItem.dayOfMonth == 1 && !changeValue) {
                //we should update visibility if it is the start day of month
                itemView.month.visibility = View.VISIBLE
            } else {
                itemView.month.visibility = View.GONE
            }
        }

        override fun bind(value: DayItem) {
            this.dayItem = value
            val selected = adapterPosition == selectedPosition

            itemView.day.text = value.dayOfMonth.toString()
            itemView.dayBackground.isSelected = selected
            itemView.day.isSelected = selected
            if (dayItem.dayOfMonth == 1 && !selected) {
                itemView.month.visibility = View.VISIBLE
                itemView.month.text = getMonthDisplayName(value.month)
            } else {
                itemView.month.visibility = View.GONE
            }

            itemView.setOnClickListener {
                if (selectedPosition == adapterPosition) {
                    //no need to update same select position
                    return@setOnClickListener
                }
                calendarSelectedBlock(dayItem)
                notifyItemChanged(selectedPosition, false)
                if (dayItem.dayOfMonth == 1) {
                    itemView.month.visibility = View.GONE
                }
                itemView.day.isSelected = true
                itemView.dayBackground.isSelected = true
                selectedPosition = adapterPosition
            }
        }
    }

}