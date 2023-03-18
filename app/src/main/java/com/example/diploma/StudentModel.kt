package com.example.diploma

import java.util.*

data class StudentModel(
    var id: Int = getAutoId(),
    var name: String ="",
    var email: String = ""
) {
    companion object {
        fun getAutoId(): Int {
            val random = Random()
            var res = random.nextInt()
            if (res < 0){
                res *= -1
            }
            return res
        }
    }
}

