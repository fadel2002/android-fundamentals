package com.dicoding.submissionone.ui.about

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.dicoding.submissionone.databinding.FragmentAboutBinding

class AboutFragment : Fragment() {
    private var _binding: FragmentAboutBinding? = null
    private val binding get() = _binding!!

    private val profileViewModel by activityViewModels<AboutViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel.userData.observe(viewLifecycleOwner) { userData ->
            setUserData(userData)
        }

        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
            (activity as AppCompatActivity).supportActionBar?.title = "About"
            (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
    }

    private fun setUserData(userData: Map<String, String>) {
        binding.tvName.text = userData["name"] ?: "-"
        binding.tvUserInterest.text = userData["interest"] ?: "-"
        binding.tvUserOrigin.text = userData["origin"] ?: "-"
        binding.tvUserEmail.text = userData["email"] ?: "-"

        Glide.with(binding.root)
            .load(userData["photo_url"])
            .placeholder(android.R.color.white)
            .into(binding.imgItemPhoto)

        binding.bVisitInstagram.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(userData["instagram"]))
            startActivity(browserIntent)
        }
        binding.bVisitGithub.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(userData["github"]))
            startActivity(browserIntent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}