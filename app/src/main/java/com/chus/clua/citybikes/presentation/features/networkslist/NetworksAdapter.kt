package com.chus.clua.citybikes.presentation.features.networkslist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.chus.clua.citybikes.databinding.ItemNetworkListBinding
import com.chus.clua.citybikes.presentation.models.NetWorkListUiModel

class NetworksAdapter(private val onItemClickListener: (String) -> Unit) :
    ListAdapter<NetWorkListUiModel, NetworkViewHolder>(NetWorkListUiModelDiffCallBack) {

    override fun onBindViewHolder(holder: NetworkViewHolder, position: Int) =
        holder.bind(getItem(position))

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        NetworkViewHolder(
            ItemNetworkListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            onItemClickListener
        )

}
