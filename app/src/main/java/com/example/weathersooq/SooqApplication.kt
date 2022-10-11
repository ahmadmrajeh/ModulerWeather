package com.example.weathersooq
import android.app.Application
import com.example.datascourcesmodule.realm_db.AppModules
import dagger.hilt.android.HiltAndroidApp
import io.realm.Realm
import io.realm.RealmConfiguration

@HiltAndroidApp
class SooqApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Realm.init (this)
        val config = RealmConfiguration.Builder().schemaVersion(4).modules(
            AppModules())
            .deleteRealmIfMigrationNeeded()
            .name("realm.db")
            .allowQueriesOnUiThread(false)
            .allowWritesOnUiThread(false)
            .build()

        Realm.setDefaultConfiguration(config)

    }

}