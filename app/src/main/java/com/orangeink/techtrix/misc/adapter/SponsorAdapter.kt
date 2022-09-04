package com.orangeink.techtrix.misc.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orangeink.techtrix.R
import com.orangeink.techtrix.databinding.RowSponsorItemBinding
import com.orangeink.techtrix.misc.data.model.Sponsor
import com.orangeink.techtrix.util.loadImage

class SponsorAdapter(
    private val mList: List<Sponsor>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_sponsor_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding: RowSponsorItemBinding =
            RowSponsorItemBinding.bind(itemView)

        fun bind(item: Sponsor) {
            item.apply {
                binding.tvSponsorTitle.text = item.name
                binding.tvSponsorDescription.text = item.description
                when (item.links.size) {
                    1 -> binding.ivFacebook.visibility = View.VISIBLE
                    2 -> {
                        binding.ivFacebook.visibility = View.VISIBLE
                        binding.ivWeb.visibility = View.VISIBLE
                    }
                    3 -> {
                        binding.ivFacebook.visibility = View.VISIBLE
                        binding.ivWeb.visibility = View.VISIBLE
                        binding.ivInstagram.visibility = View.VISIBLE
                    }
                }
                binding.ivFacebook.setOnClickListener { openLink(item.links.first()) }
                binding.ivWeb.setOnClickListener { openLink(item.links[1]) }
                binding.ivInstagram.setOnClickListener { openLink(item.links[2]) }
                item.image?.let { binding.ivSponsor.loadImage(it) }
            }
        }

        private fun openLink(link: String) {
            itemView.context.startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(link)
                )
            )
        }

    }
}