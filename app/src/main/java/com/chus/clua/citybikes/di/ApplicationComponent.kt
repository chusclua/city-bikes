package com.chus.clua.citybikes.di

import com.chus.clua.citybikes.di.modules.*
import com.chus.clua.citybikes.presentation.features.map.MapActivity
import com.chus.clua.citybikes.presentation.features.networkslist.NetWorksListActivity
import dagger.Component
import javax.inject.Singleton

@Component(
    modules = [
        ApiModule::class,
        NetworkModule::class,
        RepositoriesModule::class
    ]
)
@Singleton
interface ApplicationComponent {
    fun inject(netWorksListActivity: NetWorksListActivity)
    fun inject(mapActivity: MapActivity)
}