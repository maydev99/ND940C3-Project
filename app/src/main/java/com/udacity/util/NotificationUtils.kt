package com.udacity.util

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.R

const val NOTIFICATION_ID = 0

fun NotificationManager.sendNotification(
    messageBody: String,
    applicationContext: Context,
    repositoryDescription: String,
    downloadState: Boolean
) {

    val contentIntent = Intent(applicationContext, DetailActivity::class.java)
    val bundle = Bundle()
    bundle.putBoolean("status", downloadState)
    bundle.putString("description", repositoryDescription)
    contentIntent.putExtras(bundle)

    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val downloadImage = BitmapFactory.decodeResource(
        applicationContext.resources,
        R.drawable.downloadimage
    )

    val bigPicStyle = NotificationCompat.BigPictureStyle()
        .bigPicture(downloadImage)
        .bigLargeIcon(null)


    val builder = NotificationCompat.Builder(
        applicationContext,
        applicationContext.getString(R.string.download_notification_channel_id)
    )

        .setSmallIcon(R.drawable.ic_baseline_cloud_download_24)
        .setContentTitle(applicationContext.getString(R.string.notification_title))
        .setContentText(repositoryDescription)
        .setContentIntent(contentPendingIntent)
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .setAutoCancel(true)
        .setStyle(bigPicStyle)
        .addAction(
            R.drawable.ic_baseline_cloud_download_24,
            applicationContext.getString(R.string.view_download_status),
            contentPendingIntent
        )

    notify(NOTIFICATION_ID, builder.build())


}