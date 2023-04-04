package com.example.myflexiblefragment

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.Toast
import com.example.myflexiblefragment.databinding.FragmentCategoryBinding
import com.example.myflexiblefragment.databinding.FragmentDetailCategoryBinding
import com.google.android.material.snackbar.Snackbar

class DetailCategoryFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentDetailCategoryBinding

    var description: String? = null

    companion object {
        var EXTRA_NAME = "extra_name"
        var EXTRA_DESCRIPTION = "extra_description"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDetailCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (savedInstanceState != null) {
            val descFromBundle = savedInstanceState.getString(EXTRA_DESCRIPTION)
            description = descFromBundle
        }
        if (arguments != null) {
            val categoryName = arguments?.getString(EXTRA_NAME)
            binding.tvCategoryName.text = categoryName
            binding.tvCategoryDescription.text = description
        }

        binding.btnProfile.setOnClickListener(this)
        binding.btnShowDialog.setOnClickListener(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_DESCRIPTION, binding.tvCategoryDescription.text.toString())
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_show_dialog -> {
                val optionDialogFragment = OptionDialogFragment()
                val fragmentManager = childFragmentManager
                optionDialogFragment.show(fragmentManager, OptionDialogFragment::class.java.simpleName)
            }
            R.id.btn_profile -> {
                val intent = Intent(requireActivity(), ProfileActivity::class.java)
                startActivity(intent)
            }
        }
    }

    internal var optionDialogListener: OptionDialogFragment.OnOptionDialogListener = object : OptionDialogFragment.OnOptionDialogListener {
        override fun onOptionChosen(text: String?) {
            Toast.makeText(requireActivity(), text, Toast.LENGTH_SHORT).show()
            showSnackbar(text)
        }
    }

    private fun showSnackbar(text: String?) {
        val text_charSeq: CharSequence = text.toString()
        val snackbar = Snackbar.make(requireView(), text_charSeq, Snackbar.LENGTH_LONG)
        snackbar.setBackgroundTint(Color.BLUE)
        snackbar.setTextColor(Color.WHITE)
        snackbar.setAction("OK") { }
        snackbar.show()
    }
}