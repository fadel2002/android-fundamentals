package com.dicoding.submissionone.ui.detail

import android.content.res.Configuration
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.dicoding.submissionone.R
import com.dicoding.submissionone.data.UserResponse
import com.dicoding.submissionone.databinding.FragmentDetailBinding
import com.google.android.material.tabs.TabLayoutMediator

import android.view.View

class DetailFragment : Fragment() {
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val detailViewModel by activityViewModels<DetailViewModel>()
    private lateinit var tempValue : UserResponse
    private val maxStringOnLandscape : Int = 100
    private val maxStringOnPotrait : Int = 40

    companion object {
        private val TAB_TITLES = intArrayOf(
            R.string.title_followers,
            R.string.title_following
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
            (activity as AppCompatActivity).supportActionBar?.title = "Detail Github User"
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        val dataName = DetailFragmentArgs.fromBundle(arguments as Bundle).username

        if (dataName != detailViewModel.username.value){
            detailViewModel.loadUserData(dataName)
        }

        detailViewModel.userData.observe(viewLifecycleOwner) { userData ->
            if (userData == null){
                clearUserData()
            }else {
                tempValue = userData
                if ((activity as AppCompatActivity).applicationContext.resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE)
                    setUserData(userData, maxStringOnLandscape)

                else
                    setUserData(userData, maxStringOnPotrait)
                setUrlUserData(userData)
            }
        }

        detailViewModel.isLoadingUser.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        val sectionsPagerAdapter = DetailSectionsPagerAdapter(this)
        binding.viewPager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

        val scrollView = binding.scrollView
        val decorView = (activity as AppCompatActivity).window.decorView
        scrollView.setOnScrollChangeListener { _, _, scrollY, _, _ ->
            decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        // handle orientation change here
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setUserData(tempValue, maxStringOnLandscape)
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setUserData(tempValue, maxStringOnPotrait)
        }
    }

    private fun setUserData(userData: UserResponse?, maxLength: Int) {
        binding.tvName.text = userData?.name ?: "-"
        binding.tvUsername.text = cutString(("@" + userData?.login), maxLength)
        binding.tvUserCompany.text = cutString(userData?.company, maxLength)
        binding.tvUserOrigin.text = cutString(userData?.location, maxLength)

        val FOLLOW = listOf<String>(
            "("+detailViewModel.userData.value?.followers.toString()+")",
            "("+detailViewModel.userData.value?.following.toString()+")",
        )
        TabLayoutMediator(binding.tabs, binding.viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position]) + FOLLOW[position]
        }.attach()
    }

    private fun setUrlUserData(userData: UserResponse?){
        Glide.with(binding.root)
            .load(userData?.avatarUrl)
            .placeholder(android.R.color.white)
            .into(binding.imgItemPhoto)
    }

    private fun cutString(originalString: String?, maxLength: Int): String {
        if (originalString == null){
            return "-"
        }
        val builder = StringBuilder(originalString)
        var i = 0
        while (i + maxLength < builder.length) {
            val indexOfLastSpace = builder.substring(i, i + maxLength + 1).lastIndexOf(" ")
            if (indexOfLastSpace > 0) {
                builder.setCharAt(i + indexOfLastSpace, '\n')
            } else {
                builder.insert(i + maxLength, '\n')
            }
            i += indexOfLastSpace + 1
        }
        return builder.toString() ?: "-"
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun clearUserData(){
        binding.tvName.text = "Name"
        binding.tvUsername.text = "Username"
        binding.imgItemPhoto.setImageResource(android.R.color.white)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}