package com.boreneoux.githubuserssub1

import android.app.Application
import com.boreneoux.githubuserssub1.di.Injection

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        injection = Injection(applicationContext)
    }

    companion object {
        lateinit var injection: Injection
    }
}