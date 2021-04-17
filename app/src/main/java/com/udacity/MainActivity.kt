package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.udacity.util.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.io.File


class MainActivity : BaseActivity() {

    private var downloadID: Long = 0
    private lateinit var notificationManager: NotificationManager
    private lateinit var repositoryDescription: String
    private lateinit var loadingButton: LoadingButton
    private lateinit var downloadManager: DownloadManager
    private var downloadState = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))

        loadingButton = findViewById(R.id.custom_button)
        loadingButton.setLoadingButtonState(ButtonState.Completed)

        custom_button.setOnClickListener {
            when {
                radio_button_glide.isChecked -> {
                    repositoryDescription = getString(R.string.glide_text)
                    custom_button.isEnabled = false
                    download(GLIDE_URL)
                    loadingButton.onClickButton()
                }

                radio_button_load.isChecked -> {
                    repositoryDescription = getString(R.string.load_app_text)
                    custom_button.isEnabled = false
                    download(LOAD_APP_URL)
                    loadingButton.onClickButton()
                }

                radio_button_retrofit.isChecked -> {

                    repositoryDescription = getString(R.string.retrofit_text)
                    custom_button.isEnabled = false
                    download(RETROFIT_URL)
                    loadingButton.onClickButton()
                }

                else -> {
                    if (isAppInForeground) {
                        Toast.makeText(
                            this,
                            getString(R.string.no_repository_selected),
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                }
            }
        }

        createChannel(
            getString(R.string.download_notification_channel_id),
            getString(R.string.download_notification_channel_name)
        )
    }


    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            if (id != null) {
                val query = downloadManager.query(DownloadManager.Query().setFilterById(downloadID))
                if (query.moveToFirst()) {

                    when (query.getInt(query.getColumnIndex(DownloadManager.COLUMN_STATUS))) {
                        DownloadManager.STATUS_FAILED -> {
                            Log.i("TAG", "Download Failed")
                            downloadState = false
                            custom_button.isEnabled = true

                        }

                        DownloadManager.STATUS_SUCCESSFUL -> {
                            Log.i("TAG", "Download Successful")
                            if (isAppInForeground) {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Download Complete",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            downloadState = true
                            custom_button.isEnabled = true
                        }
                    }

                    sendNotification()
                    query.close()
                }
            }
        }
    }

    private fun sendNotification() {
        notificationManager = ContextCompat.getSystemService(
            applicationContext,
            NotificationManager::class.java
        ) as NotificationManager

        notificationManager.sendNotification(
            getString(R.string.notification_description),
            applicationContext,
            repositoryDescription,
            downloadState
        )

        radio_group.clearCheck()
        loadingButton.setLoadingButtonState(ButtonState.Completed)
    }

    private fun download(url: String) {
        val file = File(getExternalFilesDir(null), "/repo_download")

        if (!file.exists()) {
            file.mkdirs()
        }

        val request =
            DownloadManager.Request(Uri.parse(url))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "/repo_download/download.zip"
                )

        downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val LOAD_APP_URL =
            "https://github.com/udacity/nd940-c3-advanced-android-programming-project-starter/archive/master.zip"
        private const val RETROFIT_URL =
            "https://github.com/square/retrofit/archive/master.zip"
        private const val GLIDE_URL = "https://github.com/bumptech/glide/archive/master.zip"

    }


    private fun Context.createChannel(channelId: String, channelName: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                setShowBadge(false)

                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                description = getString(R.string.notification_description)
            }

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

}
