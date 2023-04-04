package com.example.myflexiblefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.commit
import com.example.myflexiblefragment.databinding.FragmentCategoryBinding

class CategoryFragment : Fragment(), View.OnClickListener {

    private lateinit var binding: FragmentCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnDetailCategory.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.btn_detail_category) {
            val detailCategoryFragment = DetailCategoryFragment()
            val bundle = Bundle()
            bundle.putString(DetailCategoryFragment.EXTRA_NAME, "Lifestyle")
            val description = "Kategori ini akan berisi produk-produk lifestyle"
            detailCategoryFragment.arguments = bundle
            detailCategoryFragment.description = description
            val fragmentManager = parentFragmentManager
//            fragmentManager?.beginTransaction()?.apply {
//                replace(R.id.frame_container, detailCategoryFragment, DetailCategoryFragment::class.java.simpleName)
//                addToBackStack(null)
//                commit()
//            }
            fragmentManager.commit {
                addToBackStack(null)
                replace(R.id.frame_container, detailCategoryFragment, DetailCategoryFragment::class.java.simpleName)
            }
        }
    }
}