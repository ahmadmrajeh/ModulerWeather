package com.example.datascourcesmodule.realm_db

import io.realm.RealmObject

open class AstronomyRlm :RealmObject  {

 var   moon_illumination: String =""
  var  moon_phase: String  =""
  var  moonrise: String  =""
  var  moonset: String  =""
  var  sunrise: String  =""
  var  sunset: String =""

    constructor(   moon_illumination: String="",
                   moon_phase: String="",
                   moonrise: String="",
                   moonset: String="",
                   sunrise: String="",
                   sunset: String="") {}

    constructor() {}
}