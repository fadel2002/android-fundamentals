package com.dicoding.localdataapplication.settingpreference

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.localdataapplication.R
import com.dicoding.localdataapplication.databinding.ActivitySettingPreferenceBinding

class SettingPreferenceActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingPreferenceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingPreferenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction().add(R.id.setting_holder, MyPreferenceFragment()).commit()
    }
}