package com.example.datascourcesmodule.realm_db

import io.realm.RealmList
import io.realm.RealmObject

open  class WeatherEntityRealm : RealmObject {

    var avgtempC: String? = ""

    var maxtempC: String?=""

    var mintempC: String?=""

    var id: String?=""

   lateinit var astronomyRlm: RealmList<AstronomyRlm>

    constructor(
        avgtempC: String? = "",

        maxtempC: String?="",

        mintempC: String?="",

        id: String?="",
        astronomyRlm: RealmList<AstronomyRlm>

    )
    constructor() {}
}