package com.example.wastemangement.DataClass

data class collectionRequest(
    val name:String,
    val uid:String,
    val biowastelist:ArrayList<String>,
    val nonbiowastelist:ArrayList<String>,
    val recwastelist:ArrayList<String>,
    val ewastelist:ArrayList<String>,
    val city:String,
    val lat:Double,
    val long:Double,
    var bioAgency:Agency,
    var nonBioAgency:Agency,
    var recAgency:Agency,
    var eAgency:Agency,
)
