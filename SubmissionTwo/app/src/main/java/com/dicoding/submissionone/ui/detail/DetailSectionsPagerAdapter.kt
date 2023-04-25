package com.dicoding.submissionone.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dicoding.submissionone.ui.detail.follow.FollowFragment

class DetailSectionsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = FollowFragment()
        val bundle = Bundle()
        when(position) {
            0 -> {
                bundle.putString("type", "follower")
            }
            1 -> {
                bundle.putString("type", "following")
            }
        }
        fragment.arguments = bundle
        return fragment
    }

    override fun getItemCount(): Int {
        return 2
    }
}