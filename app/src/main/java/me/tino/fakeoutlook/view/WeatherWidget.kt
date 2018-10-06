package me.tino.fakeoutlook.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import me.tino.fakeoutlook.ICON_MAP
import me.tino.fakeoutlook.ext.children
import me.tino.fakeoutlook.ext.dp2px
import me.tino.fakeoutlook.model.WeatherSectionInfo

/**
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 October 06, 18:27.
 */
class WeatherWidget : LinearLayout {
    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    private fun initView(context: Context) {
        orientation = VERTICAL
        setBackgroundColor(Color.WHITE)

        for (i in 0..2) {
            addView(
                WeatherSingleWidget(context, i),
                LayoutParams(LayoutParams.MATCH_PARENT, 48.dp2px())
            )
            //add divider
            if (i < 2) {
                val view = View(context)
                view.setBackgroundColor(Color.LTGRAY)
                val layoutParams = LayoutParams(LayoutParams.MATCH_PARENT, 0.5.dp2px())
                layoutParams.marginStart = 12.dp2px()
                layoutParams.marginEnd = 12.dp2px()
                addView(view, layoutParams)
            }
        }
    }

    fun updaterWeatherInfo(weatherSectionInfoList: List<WeatherSectionInfo>) {
        children.filter { it is WeatherSingleWidget }   //filter divider view
            .map { it as WeatherSingleWidget }          //smart cast to WeatherSingleWidget
            .forEachIndexed { index, widget ->
                widget.updateWeatherData(weatherSectionInfoList[index])
            }
    }

    private class WeatherSingleWidget(context: Context, index: Int) : LinearLayout(context) {
        init {
            orientation = HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL

            val titleTextView = AppCompatTextView(context)
            titleTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            titleTextView.text = when (index) {
                0 -> "Morning"
                1 -> "Afternoon"
                2 -> "Evening"
                else -> throw IndexOutOfBoundsException("index:$index is invalid")
            }
            titleTextView.setTextColor(Color.RED)
            titleTextView.setPaddingRelative(12.dp2px(), 0, 0, 0)
            addView(
                titleTextView,
                LayoutParams(0, LayoutParams.WRAP_CONTENT, 1f)
            )
            val temperatureTextView = AppCompatTextView(context)
            temperatureTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14f)
            temperatureTextView.compoundDrawablePadding = 8.dp2px()
            temperatureTextView.setPaddingRelative(0, 0, 12.dp2px(), 0)
            addView(temperatureTextView)
        }

        @SuppressLint("SetTextI18n")
        fun updateWeatherData(weatherSectionInfo: WeatherSectionInfo) {
            (getChildAt(1) as AppCompatTextView).run {
                text = "${weatherSectionInfo.temperature}°"
                setCompoundDrawablesRelativeWithIntrinsicBounds(
                    ICON_MAP[weatherSectionInfo.icon] ?: 0,
                    0,
                    0,
                    0
                )
            }
        }
    }
}