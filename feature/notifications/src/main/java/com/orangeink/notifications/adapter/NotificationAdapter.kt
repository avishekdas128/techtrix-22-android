package com.orangeink.notifications.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orangeink.network.model.Notification
import com.orangeink.notifications.R
import com.orangeink.notifications.databinding.RowNotificationItemBinding
import com.orangeink.utils.formatDate

class NotificationAdapter(
    private val mList: List<Notification>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_notification_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding: RowNotificationItemBinding = RowNotificationItemBinding.bind(itemView)

        fun bind(item: Notification) {
            item.apply {
                binding.tvTitle.text = item.title
                binding.tvDescription.text = item.description
                binding.tvDate.text = item.Id?.let { formatDate(it) }
            }
        }

    }
}