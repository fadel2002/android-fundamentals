package com.dicoding.localdataapplication.notifnavigation

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.TaskStackBuilder
import androidx.core.content.ContextCompat
import com.dicoding.localdataapplication.R
import com.dicoding.localdataapplication.databinding.ActivityNotifNavigationMainBinding

class NotifNavigationMainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityNotifNavigationMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotifNavigationMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOpenDetail.setOnClickListener(this)

        showNotification(this@NotifNavigationMainActivity, getString(R.string.notification_title), getString(R.string.notification_message), 110)

    }

    override fun onClick(v: View) {
        if (v.id == R.id.btn_open_detail) {
            val detailIntent = Intent(this@NotifNavigationMainActivity, NotifNavigationDetailActivity::class.java)
            detailIntent.putExtra(NotifNavigationDetailActivity.EXTRA_TITLE, getString(R.string.detail_title))
            detailIntent.putExtra(NotifNavigationDetailActivity.EXTRA_MESSAGE, getString(R.string.detail_message))
            startActivity(detailIntent)
        }
    }

    private fun showNotification(context: Context, title: String, message: String, notifId: Int) {
        val CHANNEL_ID = "Channel_1"
        val CHANNEL_NAME = "Navigation channel"
        val notifDetailIntent = Intent(this, NotifNavigationDetailActivity::class.java)
        notifDetailIntent.putExtra(NotifNavigationDetailActivity.EXTRA_TITLE, title)
        notifDetailIntent.putExtra(NotifNavigationDetailActivity.EXTRA_MESSAGE, message)
        val pendingIntent = TaskStackBuilder.create(this).run{
            addParentStack(NotifNavigationDetailActivity::class.java)
            addNextIntent(notifDetailIntent)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getPendingIntent(110, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
            } else {
                getPendingIntent(110, PendingIntent.FLAG_UPDATE_CURRENT)
            }
        }
        val notificationManagerCompat = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(title)
            .setSmallIcon(R.drawable.ic_email_black_24dp)
            .setContentText(message)
            .setColor(ContextCompat.getColor(context, android.R.color.black))
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSound(alarmSound)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = longArrayOf(1000, 1000, 1000, 1000, 1000)
            builder.setChannelId(CHANNEL_ID)
            notificationManagerCompat.createNotificationChannel(channel)
        }
        val notification = builder.build()
        notificationManagerCompat.notify(notifId, notification)
    }
}