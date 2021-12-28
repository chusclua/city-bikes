package com.chus.clua.citybikes.presentation.features.networkslist

import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.BackgroundColorSpan
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.chus.clua.citybikes.R
import com.chus.clua.citybikes.databinding.ItemNetworkListBinding
import com.chus.clua.citybikes.presentation.models.NetWorkListUiModel
import java.util.*

class NetworkViewHolder(
    private val binding: ItemNetworkListBinding,
    private val onItemClickListener: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(uiModel: NetWorkListUiModel) {
        uiModel.query?.let { bindName(uiModel.name, it) } ?: run {
            binding.networkName.text = uiModel.name
        }
        bindLocation("${uiModel.city}, ${uiModel.country}")
        uiModel.id?.let { bindListener(it) }
    }

    private fun bindName(name: String?, query: String) {
        val builder = SpannableStringBuilder().apply { append(name) }
        name?.toLowerCase(Locale.ROOT)?.indexOf(query.toLowerCase(Locale.ROOT))
            .takeIf { index -> index != -1 }?.let { start ->
                val colorAccent = ContextCompat.getColor(itemView.context, R.color.colorAccent)
                builder.setSpan(
                    BackgroundColorSpan(colorAccent),
                    start,
                    start + query.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
                binding.networkName.text = builder
            } ?: run {
            binding.networkName.text = name
        }

    }

    private fun bindLocation(location: String) {
        binding.networkLocation.text = location
    }

    private fun bindListener(id: String) {
        itemView.setOnClickListener { onItemClickListener(id) }
    }
}