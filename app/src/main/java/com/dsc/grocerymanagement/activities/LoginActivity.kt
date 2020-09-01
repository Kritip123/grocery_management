package com.dsc.grocerymanagement.activities

import android.app.PendingIntent.getActivity
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.dsc.grocerymanagement.R
import com.dsc.grocerymanagement.util.ConnectionManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser


class LoginActivity : AppCompatActivity() {
    private lateinit var emailTextField: EditText
    private lateinit var passFieldForEmail: EditText
    private lateinit var MobileNumber: EditText
    private lateinit var enterOtpMobile: EditText
    private lateinit var forgotPassEmail: TextView
    private lateinit var registerEmail: TextView
    private lateinit var btnLoginEmail: Button
    private lateinit var btnLoginMobile: Button
    private lateinit var btnRequestOtp: Button
    private lateinit var layoutForEmailSignIn: LinearLayout
    private lateinit var layoutForMobileSignIn: LinearLayout
    private lateinit var loginOptionGoogle: Button
    private lateinit var loginOptionEmail: Button
    private lateinit var loginOptionMobile: Button
    private var mAuth: FirebaseAuth? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        emailTextField = findViewById(R.id.emailTextField)
        MobileNumber = findViewById(R.id.MobileNumber)
        enterOtpMobile = findViewById(R.id.enterOtpMobile)
        passFieldForEmail = findViewById(R.id.passFieldForEmail)
        forgotPassEmail = findViewById(R.id.forgotPassEmail)
        registerEmail = findViewById(R.id.registerEmail)
        btnLoginEmail = findViewById(R.id.btnLoginEmail)
        btnLoginMobile = findViewById(R.id.btnLoginMobile)
        btnRequestOtp = findViewById(R.id.btnRequestOtp)
        layoutForEmailSignIn = findViewById(R.id.layoutForEmailSignIn)
        layoutForMobileSignIn = findViewById(R.id.layoutForMobileSignIn)
        mAuth = FirebaseAuth.getInstance()
        layoutForMobileSignIn.visibility = View.GONE
        layoutForEmailSignIn.visibility = View.GONE
        btnLoginMobile.visibility = View.GONE
        enterOtpMobile.visibility = View.GONE
        val customLayout: View = layoutInflater.inflate(R.layout.signin_options, null)
        loginOptionGoogle = customLayout.findViewById(R.id.loginOptionGoogle)
        loginOptionEmail = customLayout.findViewById(R.id.loginOptionEmail)
        loginOptionMobile = customLayout.findViewById(R.id.loginOptionMobile)
        val loginOption = AlertDialog.Builder(this@LoginActivity, android.R.style.Theme_Material_Light_NoActionBar_Fullscreen)
        //val loginOption = AlertDialog.Builder(this@LoginActivity)
        loginOption.setTitle("Login Options")
                .setMessage("Select your desired mode for login: ")
                .setView(customLayout)
                /*.setPositiveButton("Continue") { _, _ ->

                }*/
                .setNegativeButton("Cancel") { _, _ ->
                    finishAffinity()
                }
                .setOnCancelListener {
                    finishAffinity()
                }
        val dialog: AlertDialog = loginOption.create()
        dialog.show()
        loginOptionMobile.setOnClickListener {
            layoutForEmailSignIn.visibility = View.GONE
            layoutForMobileSignIn.visibility = View.VISIBLE
            dialog.dismiss()
        }
        loginOptionEmail.setOnClickListener {
            layoutForEmailSignIn.visibility = View.VISIBLE
            layoutForMobileSignIn.visibility = View.GONE
            dialog.dismiss()
        }
        loginOptionGoogle.setOnClickListener {
            layoutForEmailSignIn.visibility = View.GONE
            layoutForMobileSignIn.visibility = View.GONE
            dialog.dismiss()
        }
        emailTextField.setOnFocusChangeListener { _, _ ->
            if (!Patterns.EMAIL_ADDRESS.matcher(emailTextField.text.toString().trim()).matches()) {
                emailTextField.error = "Incorrect Email Address"
                //emailTextField.requestFocus()
            }
        }
        passFieldForEmail.setOnFocusChangeListener { _, _ ->
            if (!(passFieldForEmail.text.toString().length >= 6)) {
                passFieldForEmail.error = "At least 6 digits"
                //passFieldForEmail.requestFocus()
            }
        }
        btnLoginEmail.setOnClickListener {
            val email = emailTextField.text.toString().trim()
            val password = passFieldForEmail.text.toString().trim()
            if (ConnectionManager().checkConnectivity(this@LoginActivity)) {
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    if (password.length >= 6) {
                        mAuth!!.signInWithEmailAndPassword(email, password)
                                .addOnCompleteListener(this) { task ->
                                    when {
                                        task.isSuccessful -> {
                                            Toast.makeText(this@LoginActivity, "Signed In successfully",
                                                    Toast.LENGTH_SHORT).show()
                                            // Sign in success, update UI with the signed-in user's information
                                            val user = mAuth!!.currentUser
                                            //updateUI(user)
                                            val name = user?.displayName
                                            println("check $name")
                                        }
                                        task.exception is FirebaseAuthInvalidUserException -> {
                                            // If sign in fails, display a message to the user.
                                            Toast.makeText(this@LoginActivity, "User not registered\nPlease register first!",
                                                    Toast.LENGTH_SHORT).show()
                                            //updateUI(null)
                                        }
                                        task.exception is FirebaseAuthInvalidCredentialsException -> {
                                            Toast.makeText(this@LoginActivity, "Invalid Credentials!\nPlease check your password",
                                                    Toast.LENGTH_SHORT).show()
                                        }
                                        else -> {
                                            Toast.makeText(this@LoginActivity, "Failed to sign you In",
                                                    Toast.LENGTH_SHORT).show()
                                        }
                                    }

                                    // ...
                                }
                    } else {
                        passFieldForEmail.error = "Password should be at least 6 digits"
                        passFieldForEmail.requestFocus()
                    }
                } else {
                    emailTextField.error = "Invalid Email address"
                    emailTextField.requestFocus()
                }
            } else {
                val dialogNet = android.app.AlertDialog.Builder(this@LoginActivity)
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
        registerEmail.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterEmailActivity::class.java)
            startActivity(intent)
        }
        forgotPassEmail.setOnClickListener {
            val email = emailTextField.text.toString().trim()
            if (ConnectionManager().checkConnectivity(this@LoginActivity)) {
                if (Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    mAuth!!.sendPasswordResetEmail(email)
                            .addOnCompleteListener(this) { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(this@LoginActivity, "A mail has been sent with the password reset link",
                                            Toast.LENGTH_SHORT).show()
                                }else if(task.exception is FirebaseAuthInvalidUserException){
                                    Toast.makeText(this@LoginActivity, "User not registered\nPlease register first!",
                                            Toast.LENGTH_SHORT).show()
                                }
                            }
                }else{
                    emailTextField.error="Invalid Email address"
                    emailTextField.requestFocus()
                }
            } else {
                val dialogNet = android.app.AlertDialog.Builder(this@LoginActivity)
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
        btnRequestOtp.setOnClickListener {
            val mobile = MobileNumber.text.toString()
            if (mobile.length == 10) {
                btnLoginMobile.visibility = View.VISIBLE
                enterOtpMobile.visibility = View.VISIBLE
                btnRequestOtp.text = getString(R.string.Resend_OTP)
            } else {
                MobileNumber.error = "Number should be of 10 digits"
                MobileNumber.requestFocus()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        //updateUI(currentUser)
    }
}