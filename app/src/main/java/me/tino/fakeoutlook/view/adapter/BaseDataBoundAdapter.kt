package me.tino.fakeoutlook.view.adapter

import android.support.v7.recyclerview.extensions.AsyncDifferConfig
import android.support.v7.recyclerview.extensions.AsyncListDiffer
import android.support.v7.util.AdapterListUpdateCallback
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView

/**
 * a generic DataBinding adapter for RecyclerView
 * mailTo:guochenghaha@gmail.com
 * Created by tino on 2018 September 22, 18:53.
 */
abstract class BaseDataBoundAdapter<T, VH : RecyclerView.ViewHolder> :
    RecyclerView.Adapter<VH> {
    protected val listHelper: AsyncListDiffer<T>

    constructor(diffCallback: DiffUtil.ItemCallback<T>) {
        listHelper = AsyncListDiffer(
            AdapterListUpdateCallback(this),
            AsyncDifferConfig.Builder<T>(diffCallback).build()
        )
    }

    constructor(config: AsyncDifferConfig<T>) {
        listHelper = AsyncListDiffer(AdapterListUpdateCallback(this), config)
    }

    override fun getItemCount(): Int {
        return listHelper.currentList.size
    }

    fun getItem(position: Int): T {
        return listHelper.currentList[position]
    }

    fun submitList(list: List<T>) {
        listHelper.submitList(list)
    }
}