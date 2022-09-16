package com.orangeink.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.orangeink.common.findIcon
import com.orangeink.home.R
import com.orangeink.home.databinding.RowCategoryItemBinding

class CategoryAdapter(
    private val mList: List<String>,
    private val categoryInterface: CategoryInterface
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface CategoryInterface {
        fun onClick(item: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_category_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding: RowCategoryItemBinding = RowCategoryItemBinding.bind(itemView)

        fun bind(item: String) {
            item.apply {
                binding.tvCategory.text = item
                binding.ivCategoryIcon.setImageDrawable(
                    ContextCompat.getDrawable(itemView.context, findIcon(item))
                )
                binding.root.setOnClickListener {
                    categoryInterface.onClick(item)
                }
            }
        }

    }
}