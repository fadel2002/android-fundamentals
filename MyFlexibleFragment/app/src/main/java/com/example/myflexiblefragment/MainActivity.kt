package com.example.myflexiblefragment

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.commit
import com.example.myflexiblefragment.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val fragmentManager = supportFragmentManager
        val homeFragment = HomeFragment()
        val fragment = fragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)

        if (fragment !is HomeFragment) {
            Log.d("MyFlexibleFragment", "Fragment Name :" + HomeFragment::class.java.simpleName)
//            fragmentManager
//                .beginTransaction()
//                .add(R.id.frame_container, homeFragment, HomeFragment::class.java.simpleName)
//                .commit()
            fragmentManager.commit {
                add(R.id.frame_container, homeFragment, HomeFragment::class.java.simpleName)
            }
        }
    }
}