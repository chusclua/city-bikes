package com.chus.clua.citybikes.domain.usecase

import com.chus.clua.citybikes.domain.repository.NetworkRepository
import javax.inject.Inject

class GetNetWorksByCountryUseCase @Inject constructor(private val repository: NetworkRepository) {
    operator fun invoke(countryCode: String?) = repository.getNetworksByCountry(countryCode)
}