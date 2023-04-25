package com.dicoding.submissionone.ui.about

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class AboutViewModel : ViewModel() {
    private val _userData = MutableLiveData<Map<String, String>>()
    val userData: LiveData<Map<String, String>> = _userData

    init {
        loadUserData()
    }

    private fun loadUserData() {
        val data = mapOf("name" to "Fadel Pramaputra Maulana",
                                    "interest" to "Android Developer",
                                    "origin" to "Balikpapan, Indonesia",
                                    "email" to "fadelpm2002@gmail.com",
                                    "photo_url"  to "https://media.licdn.com/dms/image/C4E03AQG87LwI3q4TEA/profile-displayphoto-shrink_400_400/0/1634403891997?e=1683763200&v=beta&t=DH4PdtBCFIamx-sLx-klgmMmMnvUWuMiaNJKLaoMW9c",
                                    "instagram" to "https://www.instagram.com/pmfadel",
                                    "github" to "https://github.com/fadel2002")
        _userData.value = data
    }
}