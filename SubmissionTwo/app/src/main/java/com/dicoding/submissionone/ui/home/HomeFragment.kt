package com.dicoding.submissionone.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submissionone.R
import com.dicoding.submissionone.data.UserResponse
import com.dicoding.submissionone.databinding.FragmentHomeBinding
import com.dicoding.submissionone.helper.PreferenceViewModelFactory
import com.dicoding.submissionone.helper.SettingPreferences
import com.dicoding.submissionone.helper.ViewModelFactory
import com.dicoding.submissionone.ui.home.setting.SettingViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel by activityViewModels<HomeViewModel>{
        ViewModelFactory.getInstance(requireActivity())
    }

    private var settingViewModel: SettingViewModel? = null

    private var isDarkMode = false
    private var boolSearchFromFavorite = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId) {
                R.id.favorite -> {
                    // Respond to navigation item 1 click
                    homeViewModel.loadFavoriteListUser(viewLifecycleOwner)
                    binding.swipeRefresh.isEnabled = false
                    true
                }
                R.id.users -> {
                    // Respond to navigation item 2 click
                    if (!boolSearchFromFavorite){
                        homeViewModel.loadListUser()
                    }
                    boolSearchFromFavorite = false
                    binding.swipeRefresh.isEnabled = true
                    true
                }
                else -> false
            }
        }

        binding.bottomNavigation.setOnItemReselectedListener { item ->
            when(item.itemId) {
                R.id.favorite -> {
                    true
                }
                R.id.users -> {
                    true
                }
                else -> false
            }
        }

        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        settingViewModel = ViewModelProvider(this, PreferenceViewModelFactory(pref))[SettingViewModel::class.java]
        settingViewModel?.getThemeSettings()?.observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            isDarkMode = if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)

        val searchView = menu.findItem(R.id.search).actionView as SearchView
        var searchText = ""

        binding.swipeRefresh.setOnRefreshListener {
            if (searchText.isNullOrEmpty()){
                homeViewModel.loadListUser()
            }
            binding.swipeRefresh.isRefreshing = false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                boolSearchFromFavorite = true
                binding.bottomNavigation.selectedItemId = R.id.users
                if (query.isNotEmpty()) {
                    homeViewModel.searchUsername(query)
                    searchView.clearFocus()
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                searchText = if (newText.isNullOrEmpty()) {
                    // User has cleared the search bar
                    ""
                } else {
                    // User is typing in the search bar
                    newText
                }
                return false
            }
        })
        
        val theme = menu.findItem(R.id.theme)
        if (!isDarkMode){
            theme.icon = ContextCompat.getDrawable(requireContext(),R.drawable.baseline_light_mode_24)
        }else {
            theme.icon = ContextCompat.getDrawable(requireContext(),R.drawable.baseline_dark_mode_24)
        }

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.about -> {
                val navController = findNavController()
                navController.navigate(R.id.action_homeFragment_to_aboutFragment)
                true
            }
            R.id.theme -> {
                if (!isDarkMode){
                    item.icon = ContextCompat.getDrawable(requireContext(),R.drawable.baseline_dark_mode_24)
                    isDarkMode = true
                    settingViewModel?.saveThemeSetting(true)
                }else {
                    item.icon = ContextCompat.getDrawable(requireContext(),R.drawable.baseline_light_mode_24)
                    isDarkMode = false
                    settingViewModel?.saveThemeSetting(false)
                }
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