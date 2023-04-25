package com.dicoding.localdataapplication.notifnavigation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.localdataapplication.databinding.ActivityNotifNavigationDetailBinding

class NotifNavigationDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNotifNavigationDetailBinding

    companion object {
        const val EXTRA_TITLE = "extra_title"
        const val EXTRA_MESSAGE = "extra_message"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifNavigationDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val title = intent.getStringExtra(EXTRA_TITLE)
        val message = intent.getStringExtra(EXTRA_MESSAGE)
        binding.tvTitle.text = title
        binding.tvMessage.text = message
    }
}