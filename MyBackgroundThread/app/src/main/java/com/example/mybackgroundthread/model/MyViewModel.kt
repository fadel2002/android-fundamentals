package com.example.mybackgroundthread.model

import androidx.lifecycle.ViewModel

class MyViewModel : ViewModel(){
    var result = 0

    fun calculate(width: String, height: String, length: String) {
        result = width.toInt() * height.toInt() * length.toInt()
    }
}