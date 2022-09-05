@file:Suppress("DEPRECATION")

package com.orangeink.techtrix.home.adapter

import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.orangeink.common.findIcon
import com.orangeink.techtrix.databinding.GridEventItemBinding
import com.orangeink.techtrix.databinding.RowEventItemBinding
import com.orangeink.techtrix.event.data.model.Event
import com.orangeink.techtrix.util.constants.Constants

class EventAdapter(
    private val mList: List<Event>,
    private val eventInterface: EventInterface
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface EventInterface {
        fun onClick(item: Event)
    }

    private var viewType: Int = Constants.VIEW_LIST

    fun setViewType(viewType: Int) {
        this.viewType = viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == Constants.VIEW_LIST) {
            val binding = RowEventItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
            ListViewHolder(binding)
        } else {
            val binding = GridEventItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent, false
            )
            GridViewHolder(binding)
        }
    }

    override fun getItemCount(): Int = mList.size

    override fun getItemViewType(position: Int): Int {
        return viewType
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        mList[position].let {
            if (holder is ListViewHolder) holder.bind(it)
            else if (holder is GridViewHolder) holder.bind(it)
        }
    }

    inner class ListViewHolder(private val binding: RowEventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Event) {
            binding.apply {
                binding.tvEventName.text = item.name
                binding.tvEventDescription.text =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(item.desc, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        Html.fromHtml(item.desc)
                    }
                item.category?.let {
                    binding.ivCategoryIcon.setImageDrawable(
                        ContextCompat.getDrawable(itemView.context, findIcon(it))
                    )
                }
                binding.tvTagOne.text = item.tags[0].uppercase()
                binding.tvTagTwo.text = item.tags[1].uppercase()
                itemView.setOnClickListener {
                    eventInterface.onClick(item)
                }
            }
        }
    }

    inner class GridViewHolder(private val binding: GridEventItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Event) {
            binding.apply {
                binding.tvEventName.text = item.name
                binding.tvEventDescription.text =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        Html.fromHtml(item.desc, Html.FROM_HTML_MODE_COMPACT)
                    } else {
                        Html.fromHtml(item.desc)
                    }
                item.category?.let {
                    binding.ivCategoryIcon.setImageDrawable(
                        ContextCompat.getDrawable(itemView.context, findIcon(it))
                    )
                }
                binding.tvTagOne.text = item.tags.first().uppercase()
                itemView.setOnClickListener {
                    eventInterface.onClick(item)
                }
            }
        }
    }

}