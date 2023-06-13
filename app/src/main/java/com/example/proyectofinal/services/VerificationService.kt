package com.example.proyectofinal.services
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import com.example.proyectofinal.R
import com.example.proyectofinal.repositories.UserRepository
import java.util.*

class VerificationService : Service() {

    private var timer: Timer? = null
    private var timerTask: TimerTask? = null
    private val delay = (1 * 60 * 1000).toLong()
    private val notificationId = 1
    private val channelId = "verification_channel"
    private val channelName = "Verification Channel"
    private lateinit var userRepository: UserRepository

    override fun onCreate() {
        super.onCreate()
        userRepository = UserRepository()
        Log.d("MyBackgroundService", "Servicio creado")
        startForeground(notificationId, createNotification())
        timer = Timer()
        startTimer()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("MyBackgroundService", "Servicio onStartCommand")
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        stopTimer()
        Log.d("MyBackgroundService", "Servicio destruido")
    }

    private fun startTimer() {
        timerTask = object : TimerTask() {
            override fun run() {
                // Tarea a ejecutar después de 3 minutos
                userRepository.deleteUserAuthentication(
                    onSuccess = {
                        Log.d("MyBackgroundService", "Tarea programada ejecutada: Usuario borrado")
                    },
                    onFailure = {
                        Log.d("MyBackgroundService", "Tarea programada ejecutada: Usuario no ha podido ser borrado")
                    }
                )
            }
        }
        timer!!.schedule(timerTask, delay)
        Log.d("MyBackgroundService", "Tarea programada iniciada")
    }

    private fun stopTimer() {
        if (timerTask != null) {
            timerTask!!.cancel()
            timerTask = null
        }
        if (timer != null) {
            timer!!.cancel()
            timer!!.purge()
            timer = null
        }
        Log.d("MyBackgroundService", "Tarea programada detenida")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun createNotification(): Notification {
        createNotificationChannel()

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Servicio en segundo plano")
            .setContentText("El servicio está en ejecución.")
            .setSmallIcon(R.mipmap.ic_launcher)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        return notificationBuilder.build()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
