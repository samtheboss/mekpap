package com.mekpap.mekPap.navigation


import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.mekpap.mekPap.R
import com.mekpap.mekPap.service.ServiceUtil


class splashScreen : AppCompatActivity() {
    private lateinit var logo: ImageView
    private lateinit var preferences: SharedPreferences

    private lateinit var hearder: LinearLayout
    private lateinit var foer: LinearLayout
    private lateinit var uptodown: Animation
    private lateinit var downtoup: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        if (!isConnected(this@splashScreen)) buildDialog(this@splashScreen).show()

        super.onCreate(savedInstanceState)
        setContentView(R.layout.splashscreen)
        logo = findViewById(R.id.logo)

        hearder = findViewById(R.id.ly1)
        foer = findViewById(R.id.footer)
        uptodown = AnimationUtils.loadAnimation(this, R.anim.uptodown)
        downtoup = AnimationUtils.loadAnimation(this, R.anim.downtoup)

        hearder.animation = uptodown
        foer.animation = downtoup

        val intent = Intent(this, menu::class.java)

        preferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("FCM", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result?.token ?: throw NullPointerException("FCM token is null")


                    ServiceUtil.getFCMRegistrationTokens {
                        if (it.contains(token))
                            return@getFCMRegistrationTokens

                        it.add(token!!)
                        ServiceUtil.setFCMRegistrationTokens(it)
                    }
                })


        val timer = object : Thread() {
            override fun run() {

                try {
                    Thread.sleep(5000)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                } finally {

                    startActivity(intent)
                    finish()
                }
            }
        }

        timer.start()

    }

    fun isConnected(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnectedOrConnecting) {
            val wifi = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
            val mobile = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)

            return if (mobile != null && mobile.isConnectedOrConnecting || wifi != null && wifi.isConnectedOrConnecting)
                true
            else {
                false
            }


        } else
            return false
    }

    fun buildDialog(c: Context): AlertDialog.Builder {
        val builder = AlertDialog.Builder(c)
        builder.setTitle("No internet connection")
        builder.setMessage("Check your internet connection ")

        builder.setPositiveButton("Ok") { dialogInterface, i -> Toast.makeText(c, "make sure you have internet connection", Toast.LENGTH_SHORT).show() }
        return builder
    }

    companion object {
        private val PREF_NAME = "Shopping"
        private val KEY_EMAIL = "Email"
        private val KEY_USER_ID = "User UID"
    }
}
