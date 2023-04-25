package com.dicoding.localdataapplication.room.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.localdataapplication.databinding.ActivityNoteAddMainBinding
import com.dicoding.localdataapplication.room.helper.ViewModelFactory
import com.dicoding.localdataapplication.room.ui.insert.NoteAddUpdateActivity

class NoteAddMainActivity : AppCompatActivity() {
    private var _binding: ActivityNoteAddMainBinding? = null
    private val binding get() = _binding
    private lateinit var adapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityNoteAddMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        adapter = NoteAdapter()
        binding?.rvNotes?.layoutManager = LinearLayoutManager(this)
        binding?.rvNotes?.setHasFixedSize(true)
        binding?.rvNotes?.adapter = adapter

        val mainViewModel = obtainViewModel(this@NoteAddMainActivity)
        mainViewModel.getAllNotes().observe(this) { noteList ->
            if (noteList != null) {
                adapter.setListNotes(noteList)
            }
        }

        binding?.fabAdd?.setOnClickListener {
            val intent = Intent(this@NoteAddMainActivity, NoteAddUpdateActivity::class.java)
            startActivity(intent)
        }

        binding?.fabAdd?.setOnClickListener {
            val intent = Intent(this@NoteAddMainActivity, NoteAddUpdateActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun obtainViewModel(activity: AppCompatActivity): NoteAddMainViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(NoteAddMainViewModel::class.java)
    }
}