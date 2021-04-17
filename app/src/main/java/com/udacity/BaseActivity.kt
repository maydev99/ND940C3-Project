package com.udacity


import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity



open class BaseActivity : AppCompatActivity() {

    /**
     * Checks if App is Open so that the Download Toast
     * in the MainActivity does not show if app is closed
     */

    var isAppInForeground = false

    override fun onResume() {
        super.onResume()
        isAppInForeground = true
    }

    override fun onPause() {
        super.onPause()
        isAppInForeground = false
    }

    //Check Internet Utility

    fun isConnected(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_CELLULAR")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_WIFI")
                    return true
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    Log.i("Internet", "NetworkCapabilities.TRANSPORT_ETHERNET")
                    return true
                }
            }
        }
        return false
    }

    fun showConnectionDialog() {
        if (isAppInForeground) {
            val connectionAlert = AlertDialog.Builder(this)
            connectionAlert.apply {
                setTitle(getString(R.string.no_internet_connection))
                setMessage(getString(R.string.connection_dialog_message))
                setIcon(R.drawable.ic_baseline_signal_wifi_connected_no_internet_4_24)
                setPositiveButton(getString(R.string.close)) { _, _ ->
                    //Closes Dialog
                }
                setNegativeButton(getString(R.string.settings)) { _, _ ->
                    val intent = Intent(Settings.ACTION_DATA_ROAMING_SETTINGS)
                    context.startActivity(intent)
                }
                show()
            }
        }
    }

}