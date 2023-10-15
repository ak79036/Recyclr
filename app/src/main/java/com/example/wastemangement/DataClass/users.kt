package com.example.wastemangement.DataClass

data class users(
    var name:String="",
    val email:String="",
    val mobno:String="",
    var address:String="",
    var image:String="",
    var count:Int=0,
    val uid:String="",
    val fcmtoken:String="",
    var wasteCounts: WasteCount = WasteCount(0,0)
)
