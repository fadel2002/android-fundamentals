package com.dicoding.submissionone.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submissionone.R
import com.dicoding.submissionone.data.UserResponse
import com.dicoding.submissionone.databinding.FragmentHomeBinding


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by activityViewModels<HomeViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        if(activity is AppCompatActivity){
            (activity as AppCompatActivity).setSupportActionBar(binding.toolbar)
            (activity as AppCompatActivity).supportActionBar?.title = "Github User"
        }

        homeViewModel.listUser.observe(viewLifecycleOwner) { listUser ->
            setUserData(listUser)
        }

        val layoutManager = LinearLayoutManager(activity)
        binding.rvUser.layoutManager = layoutManager
        binding.rvUser.setHasFixedSize(true)

        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        homeViewModel.isNoUserFound.observe(viewLifecycleOwner) {
            showEmptyUsers(it)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)

        val item = menu.findItem(R.id.search).actionView as SearchView
        var searchText: String = ""

        binding.swipeRefresh.setOnRefreshListener {
            if (searchText.isNullOrEmpty()){
                homeViewModel.loadListUserPublic()
            }
            binding.swipeRefresh.isRefreshing = false
        }

        item.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    homeViewModel.searchUsername(query)
                    item.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                if (newText.isNullOrEmpty()) {
                    // User has cleared the search bar
                    searchText = ""
                } else {
                    // User is typing in the search bar
                    searchText = newText
                }
                return false
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about -> {
                val navController = findNavController()
                navController.navigate(R.id.action_homeFragment_to_aboutFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setUserData(listUser: List<UserResponse>) {
        val adapter = HomeAdapter(listUser)
        binding.rvUser.adapter = adapter
        adapter.notifyDataSetChanged()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showEmptyUsers(isNoUserFound: Boolean) {
        binding.noUserFound.visibility = if (isNoUserFound) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}