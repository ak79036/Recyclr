package com.example.wastemangement.DataClass

data class collectionRequest(
    val name:String="",
    val uid:String="",
    val biowastelist:ArrayList<String> = arrayListOf(),
    val nonbiowastelist:ArrayList<String> = arrayListOf(),
    val recwastelist:ArrayList<String> = arrayListOf(),
    val ewastelist:ArrayList<String> = arrayListOf(),
    val city:String="",
    val lat:Double=0.0,
    val long:Double=0.0,
    var bioAgency:Agency= Agency("",""),
    var nonBioAgency:Agency=Agency("",""),
    var recAgency:Agency=Agency("",""),
    var eAgency:Agency=Agency("",""),
)
