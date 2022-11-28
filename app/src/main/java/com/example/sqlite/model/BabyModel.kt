package com.example.sqlite.model

class BabyModel (
    var id: Int = getAutoId(),
    var NIK: String = "",
    var nama: String = ""
){
    companion object{
        private var autoId = 0
        fun getAutoId(): Int{
            autoId++
            return autoId
        }
    }
}