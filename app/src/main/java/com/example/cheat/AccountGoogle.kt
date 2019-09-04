package com.example.cheat

import android.app.Activity
import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn

class AccountGoogle() {
    fun singInGoogleAccount(context: Context, activity: Activity) {
        val fitnessOptions = FitnessOptions().fitnessOptions
        if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(context), fitnessOptions)) {
            GoogleSignIn.requestPermissions(
                activity,
                GOOGLE_FIT_PERMISSIONS_REQUEST_CODE,
                GoogleSignIn.getLastSignedInAccount(context),
                fitnessOptions
            )
        }
    }
}