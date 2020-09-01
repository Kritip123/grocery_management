package com.dsc.grocerymanagement

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.app.AlertDialog

class LoginActivity : AppCompatActivity() {
    lateinit var etxtEmail: EditText
    lateinit var etxtPassE: EditText
    lateinit var etxtMobile: EditText
    lateinit var etxtOtpM: EditText
    lateinit var btnLoginE: Button
    lateinit var btnLoginM: Button
    lateinit var btnOtpM: Button
    lateinit var llEmail: LinearLayout
    lateinit var llMobile: LinearLayout
    lateinit var LoginOptionGoogle: Button
    lateinit var LoginOptionEmail: Button
    lateinit var LoginOptionMobile: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        etxtEmail = findViewById(R.id.etxtEmail)
        etxtMobile = findViewById(R.id.etxtMobile)
        etxtOtpM = findViewById(R.id.etxtOtpM)
        etxtPassE = findViewById(R.id.etxtPassE)
        btnLoginE = findViewById(R.id.btnLoginE)
        btnLoginM = findViewById(R.id.btnLoginM)
        btnOtpM = findViewById(R.id.btnOtpM)
        llEmail = findViewById(R.id.llEmail)
        llMobile = findViewById(R.id.llMobile)
        llMobile.visibility = View.GONE
        llEmail.visibility = View.GONE
        btnLoginM.visibility=View.GONE
        etxtOtpM.visibility=View.GONE
        val customLayout: View = layoutInflater.inflate(R.layout.signin_options, null)
        LoginOptionGoogle = customLayout.findViewById(R.id.LoginOptionGoogle)
        LoginOptionEmail = customLayout.findViewById(R.id.LoginOptionEmail)
        LoginOptionMobile = customLayout.findViewById(R.id.LoginOptionMobile)
        val loginOption = AlertDialog.Builder(this@LoginActivity)
        loginOption.setTitle("Login Options")
                .setMessage("Select your desired mode for login: ")
                .setView(customLayout)
                /*.setPositiveButton("Continue") { _, _ ->

                }*/
                .setNegativeButton("Cancel") { _, _ ->
                    finishAffinity()
                }
        val dialog:AlertDialog=loginOption.create()
        dialog.show()
        LoginOptionMobile.setOnClickListener {
            llEmail.visibility = View.GONE
            llMobile.visibility = View.VISIBLE
            dialog.dismiss()
        }
        LoginOptionEmail.setOnClickListener {
            llEmail.visibility = View.VISIBLE
            llMobile.visibility = View.GONE
            dialog.dismiss()
        }
        LoginOptionGoogle.setOnClickListener {
            llEmail.visibility = View.GONE
            llMobile.visibility = View.GONE
            dialog.dismiss()
        }
        btnOtpM.setOnClickListener{
            btnLoginM.visibility=View.VISIBLE
            etxtOtpM.visibility=View.VISIBLE
        }
    }
}