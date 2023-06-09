package com.example.proyectofinal.handlers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AlertDialog

class InternetHandler : BroadcastReceiver() {

    companion object {
        fun isInternetAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
            connectivityManager?.let {
                val activeNetworkInfo = it.activeNetworkInfo
                return activeNetworkInfo != null && activeNetworkInfo.isConnected
            }
            return false
        }
    }

    fun createNoInternetDialog(context: Context): AlertDialog {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Sin conexión a Internet")
        builder.setMessage("No hay una conexión activa a Internet. Por favor, verifica tu conexión e intenta nuevamente.")
        builder.setPositiveButton("OK") { dialog, _ ->
            dialog.dismiss()
        }
        return builder.create()
    }

    override fun onReceive(context: Context, intent: Intent?) {

    }
}
