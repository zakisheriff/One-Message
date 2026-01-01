package com.theoneatom.onemessage.auth

import android.app.Activity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

/** Firebase Phone Authentication Manager Handles phone number verification using Firebase Auth */
class FirebasePhoneAuth(private val activity: Activity) {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private var verificationId: String? = null
    private var resendToken: PhoneAuthProvider.ForceResendingToken? = null

    /** Send OTP to the provided phone number */
    fun sendOtp(
            phoneNumber: String,
            onCodeSent: () -> Unit,
            onVerificationCompleted: () -> Unit,
            onError: (String) -> Unit
    ) {
        val callbacks =
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                        // Auto-verification (instant verification or auto-retrieval)
                        signInWithCredential(credential, onVerificationCompleted, onError)
                    }

                    override fun onVerificationFailed(e: FirebaseException) {
                        onError(e.message ?: "Verification failed")
                    }

                    override fun onCodeSent(
                            verificationId: String,
                            token: PhoneAuthProvider.ForceResendingToken
                    ) {
                        this@FirebasePhoneAuth.verificationId = verificationId
                        this@FirebasePhoneAuth.resendToken = token
                        onCodeSent()
                    }
                }

        val options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(activity)
                        .setCallbacks(callbacks)
                        .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    /** Verify the OTP code entered by user */
    fun verifyOtp(code: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        val verificationId = this.verificationId
        if (verificationId == null) {
            onError("Verification ID not found. Please request OTP again.")
            return
        }

        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithCredential(credential, onSuccess, onError)
    }

    /** Resend OTP code */
    fun resendOtp(phoneNumber: String, onCodeSent: () -> Unit, onError: (String) -> Unit) {
        val token = resendToken
        if (token == null) {
            onError("Cannot resend. Please try again.")
            return
        }

        val callbacks =
                object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    override fun onVerificationCompleted(credential: PhoneAuthCredential) {}

                    override fun onVerificationFailed(e: FirebaseException) {
                        onError(e.message ?: "Failed to resend code")
                    }

                    override fun onCodeSent(
                            verificationId: String,
                            newToken: PhoneAuthProvider.ForceResendingToken
                    ) {
                        this@FirebasePhoneAuth.verificationId = verificationId
                        this@FirebasePhoneAuth.resendToken = newToken
                        onCodeSent()
                    }
                }

        val options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(activity)
                        .setCallbacks(callbacks)
                        .setForceResendingToken(token)
                        .build()

        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun signInWithCredential(
            credential: PhoneAuthCredential,
            onSuccess: () -> Unit,
            onError: (String) -> Unit
    ) {
        auth.signInWithCredential(credential).addOnCompleteListener(activity) { task ->
            if (task.isSuccessful) {
                onSuccess()
            } else {
                onError(task.exception?.message ?: "Authentication failed")
            }
        }
    }

    /** Check if user is already signed in */
    fun isSignedIn(): Boolean = auth.currentUser != null

    /** Get current user's phone number */
    fun getCurrentUserPhone(): String? = auth.currentUser?.phoneNumber

    /** Sign out current user */
    fun signOut() = auth.signOut()
}
