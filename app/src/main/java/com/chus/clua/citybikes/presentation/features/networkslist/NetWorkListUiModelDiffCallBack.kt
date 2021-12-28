package com.chus.clua.citybikes.presentation.features.networkslist

import androidx.recyclerview.widget.DiffUtil
import com.chus.clua.citybikes.presentation.models.NetWorkListUiModel

object NetWorkListUiModelDiffCallBack : DiffUtil.ItemCallback<NetWorkListUiModel>() {
    override fun areItemsTheSame(
        oldItem: NetWorkListUiModel,
        newItem: NetWorkListUiModel
    ) = oldItem == newItem

    override fun areContentsTheSame(
        oldItem: NetWorkListUiModel,
        newItem: NetWorkListUiModel
    ) = oldItem.id == newItem.id && oldItem.query == newItem.query
}