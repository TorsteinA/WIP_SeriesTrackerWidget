package com.example.seriestracker.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.seriestracker.ITEM_VIEW_TYPE_HEADER
import com.example.seriestracker.ITEM_VIEW_TYPE_LIST_ENTITY_ITEM_E_ONLY
import com.example.seriestracker.ITEM_VIEW_TYPE_LIST_ENTITY_ITEM_S_PLUS_E
import com.example.seriestracker.R
import com.example.seriestracker.database.SeriesListEntity
import com.example.seriestracker.databinding.ListItemSeriesBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.ClassCastException

class SeriesListEntityAdapter(val clickListener: SeriesListEntityListener) : ListAdapter<DataItem, RecyclerView.ViewHolder>(SeriesListEntityDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType){
            ITEM_VIEW_TYPE_HEADER -> TextViewHolder.from(parent)
            ITEM_VIEW_TYPE_LIST_ENTITY_ITEM_S_PLUS_E -> ViewHolder.from(parent)
            ITEM_VIEW_TYPE_LIST_ENTITY_ITEM_E_ONLY -> ViewHolder.from(parent)   // TODO Add support for both types of list items
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val listItem = getItem(position) as DataItem.SeriesListEntityItemSPlusE
                holder.bind(listItem.seriesListEntity, clickListener)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)){
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.SeriesListEntityItemSPlusE -> ITEM_VIEW_TYPE_LIST_ENTITY_ITEM_S_PLUS_E
            is DataItem.SeriesListEntityItemEOnly -> ITEM_VIEW_TYPE_LIST_ENTITY_ITEM_E_ONLY
        }
    }

    fun addHeaderAndSubmitList(list: List<SeriesListEntity>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.SeriesListEntityItemSPlusE(it) }          // TODO Add support for E_only items
            }
            withContext(Dispatchers.Main){
                submitList(items)
            }
        }
    }

    class ViewHolder private constructor(val binding: ListItemSeriesBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: SeriesListEntity, clickListener: SeriesListEntityListener) {
            binding.entity = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from (parent: ViewGroup) : ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemSeriesBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class SeriesListEntityDiffCallback : DiffUtil.ItemCallback<DataItem>() {

    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.equals(newItem)
    }
}

class SeriesListEntityListener(val clickListener: (entityId: Long) -> Unit) {
    fun onClickedText(entity: SeriesListEntity) = clickListener(entity.entityId)

    fun onClickedSeason(entity: SeriesListEntity) = clickListener(entity.entityId)

    fun onClickedEpisode(entity: SeriesListEntity) = clickListener(entity.entityId)

    fun onClickedCheckmark(entity: SeriesListEntity) = clickListener(entity.entityId)
}

sealed class DataItem {

    abstract val id: Long

    data class SeriesListEntityItemSPlusE (val seriesListEntity: SeriesListEntity): DataItem() {
        override val id = seriesListEntity.entityId
    }

    data class SeriesListEntityItemEOnly (val seriesListEntity: SeriesListEntity): DataItem() {
        override val id = seriesListEntity.entityId
    }

    object Header: DataItem() {
        override val id = Long.MIN_VALUE
    }
}

class TextViewHolder(view: View): RecyclerView.ViewHolder(view){
    companion object {
        fun from(parent: ViewGroup): TextViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.header, parent, false)
            return TextViewHolder(view)
        }
    }
}