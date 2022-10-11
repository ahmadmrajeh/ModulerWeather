package com.example.datascourcesmodule.realm_db

 import io.realm.annotations.RealmModule


@RealmModule(  classes = [WeatherEntityRealm::class, AstronomyRlm::class])
class AppModules {

}