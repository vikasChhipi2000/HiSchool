package com.example.hischool

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_sign_up.*


class SignUp : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
    }

    fun nextClick(view: View) {
        val phoneNo = phoneNumberEditText.text.toString().trim()
        if (phoneNo.isEmpty() || phoneNo.length < 10) {
            phoneNumberEditText.error = "Enter a valid mobile"
            phoneNumberEditText.requestFocus()
            return
        }
        val intent = Intent(applicationContext, VerifyPhoneActivity::class.java)
        intent.putExtra("phoneNo", phoneNo)
        startActivity(intent)
    }
}