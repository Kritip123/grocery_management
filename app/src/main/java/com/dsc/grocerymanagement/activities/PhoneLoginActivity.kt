package com.dsc.grocerymanagement.activities

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dsc.grocerymanagement.R
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class PhoneLoginActivity : AppCompatActivity() {
    private lateinit var MobileNumber: EditText
    private lateinit var enterOtpMobile: EditText
    private lateinit var btnLoginMobile: Button
    private lateinit var btnRequestOtp: Button
    private var storedVerificationId: String = "0"
    private var mAuth: FirebaseAuth? = null
    private var pAuth: PhoneAuthProvider? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_phone_login)
        MobileNumber = findViewById(R.id.MobileNumber)
        enterOtpMobile = findViewById(R.id.enterOtpMobile)
        btnLoginMobile = findViewById(R.id.btnLoginMobile)
        btnRequestOtp = findViewById(R.id.btnRequestOtp)
        btnLoginMobile.visibility = View.GONE
        enterOtpMobile.visibility = View.GONE
        mAuth = FirebaseAuth.getInstance()
        pAuth = PhoneAuthProvider.getInstance()
        btnRequestOtp.setOnClickListener {
            val mobile = MobileNumber.text.toString()
            if (mobile.length >= 10) {
                pAuth!!.verifyPhoneNumber(
                        mobile, // Phone number to verify
                        60, // Timeout duration
                        TimeUnit.SECONDS, // Unit of timeout
                        this, // Activity (for callback binding)
                        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                                Toast.makeText(this@PhoneLoginActivity, "You have been successfully verified!",
                                        Toast.LENGTH_SHORT).show()
                            }

                            override fun onVerificationFailed(e: FirebaseException) {
                                if (e is FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(this@PhoneLoginActivity, "Invalid phone number",
                                            Toast.LENGTH_SHORT).show()
                                } else if (e is FirebaseTooManyRequestsException) {
                                    Toast.makeText(this@PhoneLoginActivity, "The SMS quota for the project has been exceeded",
                                            Toast.LENGTH_SHORT).show()
                                } else {
                                    Toast.makeText(this@PhoneLoginActivity, "Some error occurred",
                                            Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onCodeSent(
                                    verificationId: String,
                                    forceResendingToken: PhoneAuthProvider.ForceResendingToken
                            ) {
                                Toast.makeText(this@PhoneLoginActivity, "OTP has been sent to your mobile!",
                                        Toast.LENGTH_SHORT).show()
                                storedVerificationId = verificationId
                                this@PhoneLoginActivity.enableUserManuallyInputCode()
                            }
                        }) // OnVerificationStateChangedCallbacks

            } else {
                MobileNumber.error = "Number should be of 10 digits"
                MobileNumber.requestFocus()
            }
        }
        btnLoginMobile.setOnClickListener {
            val otp = enterOtpMobile.text.toString().trim()
            if (otp.length == 6) {
                val credential = PhoneAuthProvider.getCredential(storedVerificationId, otp)
                mAuth!!.signInWithCredential(credential)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(this@PhoneLoginActivity, "Logged In successfully",
                                        Toast.LENGTH_SHORT).show()
                                val user = task.result?.user
                                // ...
                            } else if (task.exception is FirebaseAuthInvalidCredentialsException) {
                                // Sign in failed, display a message and update the UI
                                Toast.makeText(this@PhoneLoginActivity, "Incorrect OTP!",
                                        Toast.LENGTH_SHORT).show()
                                // The verification code entered was invalid
                            } else {
                                Toast.makeText(this@PhoneLoginActivity, "Some error occurred!!",
                                        Toast.LENGTH_SHORT).show()
                            }
                        }
            } else {
                enterOtpMobile.error = "Invalid OTP"
                enterOtpMobile.requestFocus()
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth!!.currentUser
        //updateUI(currentUser)
    }

    fun enableUserManuallyInputCode() {
        btnLoginMobile.visibility = View.VISIBLE
        enterOtpMobile.visibility = View.VISIBLE
        btnRequestOtp.text = getString(R.string.Resend_OTP)
    }
}