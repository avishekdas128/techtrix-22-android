package com.orangeink.techtrix.registrations.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.orangeink.common.findIcon
import com.orangeink.techtrix.R
import com.orangeink.techtrix.databinding.RowRegistrationItemBinding
import com.orangeink.network.model.Registration

class RegistrationAdapter(
    private val mList: List<Registration>,
    private val registrationInterface: RegistrationInterface
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface RegistrationInterface {
        fun onClick(item: Registration)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = RowRegistrationItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ListViewHolder(binding)
    }

    override fun getItemCount(): Int = mList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        mList[position].let {
            if (holder is ListViewHolder) holder.bind(it)
        }
    }

    inner class ListViewHolder(private val binding: RowRegistrationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Registration) {
            binding.apply {
                binding.tvEventDescription.text = item.teamName
                binding.tvEventName.text = item.eventName
                item.eventCategory?.let {
                    binding.ivCategoryIcon.setImageDrawable(
                        ContextCompat.getDrawable(itemView.context, findIcon(it))
                    )
                }
                binding.tvTagOne.text =
                    if (item.paid!!)
                        itemView.context.getString(R.string.paid)
                    else
                        itemView.context.getString(R.string.unpaid)
                itemView.setOnClickListener {
                    registrationInterface.onClick(item)
                }
            }
        }
    }

}