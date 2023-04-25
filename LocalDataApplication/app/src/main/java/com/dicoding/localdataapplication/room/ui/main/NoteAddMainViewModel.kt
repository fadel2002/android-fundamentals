package com.dicoding.localdataapplication.room.ui.main

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.localdataapplication.room.database.Note
import com.dicoding.localdataapplication.room.repository.NoteRepository

class NoteAddMainViewModel(application: Application) : ViewModel() {
    private val mNoteRepository: NoteRepository = NoteRepository(application)
    fun getAllNotes(): LiveData<List<Note>> = mNoteRepository.getAllNotes()
}