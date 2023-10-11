package com.example.xpay

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import com.google.firebase.auth.FirebaseAuth

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        val Main = Intent(this, Home::class.java)

        val motionLayout  = findViewById<MotionLayout>(R.id.SplashScreen)
        motionLayout.addTransitionListener(object : MotionLayout.TransitionListener{
            override fun onTransitionStarted(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int
            ) {
            }

            override fun onTransitionChange(
                motionLayout: MotionLayout?,
                startId: Int,
                endId: Int,
                progress: Float
            ) {
            }

            override fun onTransitionCompleted(motionLayout: MotionLayout?, currentId: Int) {
                lateinit var auth: FirebaseAuth
                auth = FirebaseAuth.getInstance()
                if (auth.currentUser != null) {
                    startActivity(Main)
                    finish()
                }
                else {
                    startActivity(Intent(this@SplashScreen, LoginPage::class.java))
                    finish()
                }
            }

            override fun onTransitionTrigger(
                motionLayout: MotionLayout?,
                triggerId: Int,
                positive: Boolean,
                progress: Float
            ) {
            }


        })


    }
}
