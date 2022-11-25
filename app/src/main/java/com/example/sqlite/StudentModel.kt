package com.example.sqlite

import java.util.*

class StudentModel (
    var id: Int = getAutoId(),
    var name: String = "",
    var email: String = ""
){
    companion object{
        private var autoId = 0
        fun getAutoId(): Int{
            autoId++
            return autoId
        }
    }
}