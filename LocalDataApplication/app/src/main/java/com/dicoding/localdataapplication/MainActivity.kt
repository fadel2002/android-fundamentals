package com.dicoding.localdataapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.localdataapplication.alarmmanager.AlarmOneTimeMainActivity
import com.dicoding.localdataapplication.broadcastreceiver.BroadcastReceiverMainActivity
import com.dicoding.localdataapplication.databinding.ActivityMainBinding
import com.dicoding.localdataapplication.datastore.DataStoreActivity
import com.dicoding.localdataapplication.filestorage.FileStorageActivity
import com.dicoding.localdataapplication.notifnavigation.NotifNavigationMainActivity
import com.dicoding.localdataapplication.room.ui.main.NoteAddMainActivity
import com.dicoding.localdataapplication.settingpreference.SettingPreferenceActivity
import com.dicoding.localdataapplication.sharedpreferences.SharedPreferencesActivity
import com.dicoding.localdataapplication.simplenotif.SimpleNotifMainActivity
import com.dicoding.localdataapplication.sqlite.SQLiteMainActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bToFileStorage.setOnClickListener{
            startActivity(Intent(this@MainActivity, FileStorageActivity::class.java))
        }
        binding.bToSharedPreferences.setOnClickListener{
            startActivity(Intent(this@MainActivity, SharedPreferencesActivity::class.java))
        }
        binding.bToSettingPreference.setOnClickListener{
            startActivity(Intent(this@MainActivity, SettingPreferenceActivity::class.java))
        }
        binding.bToDataStore.setOnClickListener{
            startActivity(Intent(this@MainActivity, DataStoreActivity::class.java))
        }
        binding.bToSQLite.setOnClickListener{
            startActivity(Intent(this@MainActivity, SQLiteMainActivity::class.java))
        }
        binding.bToRoom.setOnClickListener{
            startActivity(Intent(this@MainActivity, NoteAddMainActivity::class.java))
        }
        binding.bToBroadcast.setOnClickListener{
            startActivity(Intent(this@MainActivity, BroadcastReceiverMainActivity::class.java))
        }
        binding.bToSimpleNotif.setOnClickListener{
            startActivity(Intent(this@MainActivity, SimpleNotifMainActivity::class.java))
        }
        binding.bToNotifNavigation.setOnClickListener{
            startActivity(Intent(this@MainActivity, NotifNavigationMainActivity::class.java))
        }
        binding.bToAlarmOneTime.setOnClickListener{
            startActivity(Intent(this@MainActivity, AlarmOneTimeMainActivity::class.java))
        }
    }
}