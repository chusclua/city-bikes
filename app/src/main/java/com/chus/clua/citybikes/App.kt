package com.chus.clua.citybikes

import android.app.Application
import com.chus.clua.citybikes.di.ApplicationComponent
import com.chus.clua.citybikes.di.DaggerApplicationComponent

class App : Application() {

    private var applicationComponent: ApplicationComponent? = null

    open fun getComponent(): ApplicationComponent? {
        applicationComponent?.let {
            return it
        } ?: run {
            applicationComponent = DaggerApplicationComponent.builder()
                .build()
            return applicationComponent
        }
    }
}