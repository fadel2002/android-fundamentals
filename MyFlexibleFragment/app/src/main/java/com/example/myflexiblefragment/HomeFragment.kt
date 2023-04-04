package com.example.myflexiblefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.example.myflexiblefragment.databinding.FragmentHomeBinding

class HomeFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnCategory.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_category -> {
                val categoryFragment = CategoryFragment()
                val fragmentManager = parentFragmentManager
//                fragmentManager.beginTransaction().apply {
//                    replace(R.id.frame_container, categoryFragment, CategoryFragment::class.java.simpleName)
//                    addToBackStack(null)
//                    commit()
//                }
                fragmentManager.commit {
                    addToBackStack(null)
                    replace(R.id.frame_container, categoryFragment, CategoryFragment::class.java.simpleName)
                }
            }
        }
    }
}