package me.tino.fakeoutlook.view.adapter

import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.item_week.view.*
import me.tino.fakeoutlook.R

/**
 * the
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 September 23, 20:41.
 */
class WeekAdapter(private val list: List<String>): RecyclerView.Adapter<WeekAdapter.WeekViewHolder>() {
    override fun getItemCount(): Int {
        return list.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_week, parent, false)
        return WeekViewHolder(view)
    }

    override fun onBindViewHolder(holder: WeekViewHolder, position: Int) {
        holder.bind(list[position])
    }

    class WeekViewHolder(view: View) : RecyclerView.ViewHolder(view), ViewBind<String> {
        override fun bind(value: String) {
            itemView.weekItem.text = value
            if (adapterPosition == 0 || adapterPosition == 6) {
                itemView.weekItem.setTextColor(Color.argb(127, 255,255,255))
            }
        }
    }
}