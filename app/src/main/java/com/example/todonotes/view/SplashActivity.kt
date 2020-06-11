package com.example.todonotes.view



import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.todonotes.utils.PrefConstant
import com.example.todonotes.R
import com.example.todonotes.onboarding.OnBoardingActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId

class SplashActivity : AppCompatActivity() {

    lateinit var sharedPreferences : SharedPreferences
    lateinit var thread: Thread


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupSharedPreferences()
        showSplash()
        getFCMToken()
    }

    private fun showSplash() {
        thread = Thread(Runnable {
            kotlin.run {
                Thread.sleep(2500)
                checkLoginStatus()
            }
        })
        thread.start()
    }

    private fun getFCMToken() {

        FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("SplashActivity", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    val token = task.result!!.token

                    // Log and toast
                    //val msg = getString(R.string.msg_token_fmt, token)
                    Log.d("SplashActivity", token)
                    //Toast.makeText(baseContext, token, Toast.LENGTH_SHORT).show()
                })
    }

    private fun setupSharedPreferences() {
        sharedPreferences = getSharedPreferences(PrefConstant.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    private fun checkLoginStatus() {

        val isLoggedIn = sharedPreferences.getBoolean(PrefConstant.IS_LOGGED_IN,false)
        val isBoardingSuccessfully = sharedPreferences.getBoolean(PrefConstant.ON_BOARDED_SUCCESSFULLY,false)

        if(isLoggedIn){

            val intent = Intent(this@SplashActivity, MyNotesActivity::class.java)
            startActivity(intent)
            finish()
        }else{
            if (isBoardingSuccessfully) {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this@SplashActivity, OnBoardingActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}