package com.chus.clua.citybikes.domain.usecase

import com.chus.clua.citybikes.domain.repository.NetworkRepository
import javax.inject.Inject

class GetAllNetWorksUseCase @Inject constructor(private val repository: NetworkRepository) {
    operator fun invoke() = repository.getNetworks()
}