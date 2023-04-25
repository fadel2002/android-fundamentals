package com.dicoding.submissionone.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.*
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
                    homeViewModel.loadListUser()
                    binding.swipeRefresh.isEnabled = true
                    true
                }
                else -> false
            }
        }

        binding.bottomNavigation.setOnItemReselectedListener { item ->
            when(item.itemId) {
                R.id.favorite -> {
                    // Respond to reselected item 1 click
                    true
                }
                R.id.users -> {
                    // Respond to reselected item 2 click
                    true
                }
                else -> false
            }
        }

        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        val settingViewModel = ViewModelProvider(this, PreferenceViewModelFactory(pref)).get(
            SettingViewModel::class.java
        )
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }
    }

    var test = 2

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.home_menu, menu)

        val searchView = menu.findItem(R.id.search).actionView as SearchView
        var searchText: String = ""

        binding.swipeRefresh.setOnRefreshListener {
            if (searchText.isNullOrEmpty()){
                homeViewModel.loadListUser()
            }
            binding.swipeRefresh.isRefreshing = false
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    homeViewModel.searchUsername(query)
                    searchView.clearFocus()
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
        
        val theme = menu.findItem(R.id.theme)
        if (test == 1){
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
                if (test == 1){
                    item.icon = ContextCompat.getDrawable(requireContext(),R.drawable.baseline_dark_mode_24)
                    test = 2
                }else {
                    item.icon = ContextCompat.getDrawable(requireContext(),R.drawable.baseline_light_mode_24)
                    test = 1
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