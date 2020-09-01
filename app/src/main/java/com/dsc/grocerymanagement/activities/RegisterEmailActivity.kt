package com.dsc.grocerymanagement.activities

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dsc.grocerymanagement.R
import com.dsc.grocerymanagement.util.ConnectionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.UserProfileChangeRequest


class RegisterEmailActivity : AppCompatActivity() {
    private lateinit var btnRegister: Button
    private lateinit var etName: EditText
    private lateinit var etPhoneNumber: EditText
    private lateinit var etPassword: EditText
    private lateinit var etEmail: EditText
    private lateinit var etAddress: EditText
    private lateinit var etConfirmPassword: EditText
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register_email)
        etName = findViewById(R.id.etName)
        etPhoneNumber = findViewById(R.id.etPhoneNumber)
        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        etAddress = findViewById(R.id.etAddress)
        btnRegister = findViewById(R.id.btnRegister)
        mAuth = FirebaseAuth.getInstance()
        etEmail.setOnFocusChangeListener { _, _ ->
            if (!Patterns.EMAIL_ADDRESS.matcher(etEmail.text.toString().trim()).matches()) {
                etEmail.error = "Incorrect Email Address"
                //etEmail.requestFocus()
            }
        }
        etPassword.setOnFocusChangeListener { _, _ ->
            if (etPassword.text.toString().trim().length < 6) {
                etPassword.error = "Password should be of at least 6 digits"
                //etPassword.requestFocus()
            }
        }
        etConfirmPassword.setOnFocusChangeListener { _, _ ->
            if (etConfirmPassword.text.toString().trim() != etPassword.text.toString().trim()) {
                etConfirmPassword.error = "Password should be of at least 6 digits"
                //etConfirmPassword.requestFocus()
            }
        }
        btnRegister.setOnClickListener {
            val name=etName.text.toString()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()
            if (ConnectionManager().checkConnectivity(this@RegisterEmailActivity)) {
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (name!=null&&name.length>=4) {
                        if (password.length >= 6) {
                            if (etPassword.text.toString().trim() == etConfirmPassword.text.toString().trim()) {
                                mAuth!!.createUserWithEmailAndPassword(email, password)
                                        .addOnCompleteListener(this) { task ->
                                            when {
                                                task.isSuccessful -> {
                                                    // Sign in success, update UI with the signed-in user's information
                                                    val user = mAuth!!.currentUser
                                                    Toast.makeText(this@RegisterEmailActivity, "Registered Successfully",
                                                            Toast.LENGTH_SHORT).show()
                                                    val profileUpdates = UserProfileChangeRequest.Builder()
                                                            .setDisplayName(name)
                                                            .build()
                                                    user!!.updateProfile(profileUpdates)
                                                            .addOnCompleteListener(this) { update ->
                                                                if (update.isSuccessful) {
                                                                    Toast.makeText(this@RegisterEmailActivity, "Details uploaded",
                                                                            Toast.LENGTH_SHORT).show()
                                                                } else {
                                                                    Toast.makeText(this@RegisterEmailActivity, "Upload Failed",
                                                                            Toast.LENGTH_SHORT).show()
                                                                }
                                                            }

                                                    user.sendEmailVerification()
                                                            .addOnCompleteListener(this@RegisterEmailActivity){verify->
                                                                if(verify.isSuccessful){
                                                                    Toast.makeText(this@RegisterEmailActivity, "Verification Link sent",
                                                                            Toast.LENGTH_SHORT).show()
                                                                }else{
                                                                    Toast.makeText(this@RegisterEmailActivity, "Your account is not verified",
                                                                            Toast.LENGTH_SHORT).show()
                                                                }
                                                            }
                                                    //updateUI(user)
                                                }
                                                task.exception is FirebaseAuthUserCollisionException -> {
                                                    // If sign in fails, display a message to the user.
                                                    Toast.makeText(this@RegisterEmailActivity, "Email Address is already registered",
                                                            Toast.LENGTH_SHORT).show()
                                                    //updateUI(null)
                                                }
                                                else -> {
                                                    Toast.makeText(this@RegisterEmailActivity, "Some error has occurred",
                                                            Toast.LENGTH_SHORT).show()
                                                }
                                            }

                                            // ...
                                        }
                            } else {
                                etConfirmPassword.error = "Password doesn't match"
                                etConfirmPassword.requestFocus()
                            }
                        } else {
                            etPassword.error = "Password should be at least 6 digits"
                            etPassword.requestFocus()
                        }
                    }else{
                        etName.error = "Enter appropriate Name"
                        etName.requestFocus()
                    }
                } else {
                    etEmail.error = "Incorrect Email address"
                    etEmail.requestFocus()
                }
            } else {
                val dialogNet = android.app.AlertDialog.Builder(this@RegisterEmailActivity)
                dialogNet.setTitle("Error")
                dialogNet.setMessage("Internet Connection is not Found")
                dialogNet.setPositiveButton("Open Settings") { _, _ ->
                    val settingsIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingsIntent)
                    finish()
                }
                dialogNet.setNegativeButton("Exit") { _, _ ->
                    finishAffinity()
                }
                        .setOnCancelListener {
                            finishAffinity()
                        }
                dialogNet.create()
                dialogNet.show()
            }
        }
    }
}