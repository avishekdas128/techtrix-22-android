package com.orangeink.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orangeink.search.R
import com.orangeink.search.databinding.RowTrendingSearchItemBinding

class TrendingSearchAdapter(
    private val mList: List<String>,
    private val trendingInterface: TrendingInterface
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface TrendingInterface {
        fun onClick(item: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_trending_search_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding: RowTrendingSearchItemBinding =
            RowTrendingSearchItemBinding.bind(itemView)

        fun bind(item: String) {
            item.apply {
                binding.tvSearch.text = item
                binding.root.setOnClickListener {
                    trendingInterface.onClick(item)
                }
            }
        }

    }
}