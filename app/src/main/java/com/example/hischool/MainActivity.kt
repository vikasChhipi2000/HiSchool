package com.example.hischool

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var top: Animation? = null
    private var bottom:Animation? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        top = AnimationUtils.loadAnimation(this, R.anim.topanimation)
        bottom = AnimationUtils.loadAnimation(this, R.anim.bottomanimation)

        schoolImageView.animation = top
        logoTextView.animation = bottom

        Handler().postDelayed({
            if(FirebaseAuth.getInstance().currentUser != null) {
                val intent = Intent(this@MainActivity, MainMenu::class.java)
                startActivity(intent)
                finish()
            }else{
                val intent = Intent(this@MainActivity, SignUp::class.java)
                startActivity(intent)
                finish()
            }
        }, 4000)
    }
}