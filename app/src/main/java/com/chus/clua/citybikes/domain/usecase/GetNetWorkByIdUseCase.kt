package com.chus.clua.citybikes.domain.usecase

import com.chus.clua.citybikes.domain.repository.NetworkRepository
import javax.inject.Inject

class GetNetWorkByIdUseCase @Inject constructor(private val repository: NetworkRepository) {
    operator fun invoke(netWorkId: String?) = repository.getNetworkById(netWorkId)
}