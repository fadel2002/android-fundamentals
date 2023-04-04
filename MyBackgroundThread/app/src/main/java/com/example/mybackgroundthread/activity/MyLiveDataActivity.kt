package com.example.mybackgroundthread.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mybackgroundthread.R
import com.example.mybackgroundthread.databinding.ActivityMyLiveDataBinding
import com.example.mybackgroundthread.model.MyLiveDataModel

class MyLiveDataActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMyLiveDataBinding
//    private lateinit var liveDataTimerViewModel: MyLiveDataModel
    private val viewModel: MyLiveDataModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyLiveDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        liveDataTimerViewModel = ViewModelProvider(this)[MyLiveDataModel::class.java]
        subscribe()
    }

    private fun subscribe() {
//        val elapsedTimeObserver = Observer<Long?> { aLong ->
//            val newText = this@MyLiveDataActivity.resources.getString(R.string.seconds, aLong)
//            binding.timerTextview.text = newText
//        }
//        viewModel.getElapsedTime().observe(this, elapsedTimeObserver)

        viewModel.getElapsedTime().observe(this,
            Observer<Long?> {
                val newText = this@MyLiveDataActivity.resources.getString(R.string.seconds, it)
                binding.timerTextview.text = newText
        })
    }
}