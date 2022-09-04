package com.orangeink.techtrix.misc.adapter

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.orangeink.techtrix.R
import com.orangeink.techtrix.databinding.RowTeamItemBinding
import com.orangeink.techtrix.misc.data.model.Team
import com.orangeink.techtrix.util.loadImage

class TeamAdapter(
    private val mList: List<Team>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_team_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as ViewHolder).bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val binding: RowTeamItemBinding =
            RowTeamItemBinding.bind(itemView)

        fun bind(item: Team) {
            item.apply {
                binding.tvName.text = item.name
                binding.tvRole.text = item.role
                when (item.links.size) {
                    0 -> {
                        binding.ivFacebook.visibility = View.GONE
                        binding.ivLinkedin.visibility = View.GONE
                    }
                    1 -> {
                        binding.ivFacebook.visibility = View.VISIBLE
                        binding.ivLinkedin.visibility = View.GONE
                    }
                    2 -> {
                        binding.ivFacebook.visibility = View.VISIBLE
                        binding.ivLinkedin.visibility = View.VISIBLE
                    }
                }
                binding.ivFacebook.setOnClickListener { openLink(item.links.first()) }
                binding.ivLinkedin.setOnClickListener { openLink(item.links[1]) }
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