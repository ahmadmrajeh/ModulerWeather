package com.example.modelsmodule.data

data class Data(
    val ClimateAverages: List<ClimateAverage>,
    val current_condition: List<CurrentCondition>,
    val request: List<Request>,
    val weather: List<Weather>
)