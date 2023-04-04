package com.dicoding.submissionone.ui.detail.follow

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submissionone.data.UserResponse
import com.dicoding.submissionone.databinding.FragmentFollowBinding
import com.dicoding.submissionone.ui.detail.DetailViewModel

class FollowFragment : Fragment() {
    private var _binding: FragmentFollowBinding? = null
    private val binding get() = _binding!!
    private val detailViewModel by activityViewModels<DetailViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = arguments?.getString("type")
        if(type == "following"){
            detailViewModel.listFollowing.observe(viewLifecycleOwner) { listFollowing ->
                if (listFollowing == null){
                    clearUserFollow()
                }else {
                    setListUserFollowData(listFollowing)
                }
            }
            detailViewModel.userData.observe(viewLifecycleOwner) { userData ->
                detailViewModel.loadListFollowing(userData?.login ?: "")
            }
            detailViewModel.isLoadingFollowing.observe(viewLifecycleOwner) {
                showLoading(it)
            }
            detailViewModel.isNoFollowingFound.observe(viewLifecycleOwner) {
                showEmptyUsers(it)
            }
        }else {
            detailViewModel.listFollowers.observe(viewLifecycleOwner) { listFollower ->
                if (listFollower == null){
                    clearUserFollow()
                }else {
                    setListUserFollowData(listFollower)
                }
            }
            detailViewModel.userData.observe(viewLifecycleOwner) { userData ->
                detailViewModel.loadListFollower(userData?.login ?: "")
            }
            detailViewModel.isLoadingFollower.observe(viewLifecycleOwner) {
                showLoading(it)
            }
            detailViewModel.isNoFollowerFound.observe(viewLifecycleOwner) {
                showEmptyUsers(it)
            }
        }

        val layoutManager = LinearLayoutManager(activity)
        binding.rvUserFollow.layoutManager = layoutManager
        binding.rvUserFollow.setHasFixedSize(true)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setListUserFollowData(listFollower: List<UserResponse>) {
        val adapter = FollowAdapter(listFollower)
        binding.rvUserFollow.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun clearUserFollow(){
        val adapter = FollowAdapter(listOf())
        binding.rvUserFollow.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.INVISIBLE
    }

    private fun showEmptyUsers(isNoUserFound: Boolean) {
        binding.noUserFound.visibility = if (isNoUserFound) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}