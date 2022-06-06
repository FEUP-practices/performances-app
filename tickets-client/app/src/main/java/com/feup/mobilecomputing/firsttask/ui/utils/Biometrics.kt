package com.feup.mobilecomputing.firsttask.ui.utils

import android.content.Context
import androidx.biometric.BiometricManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK

@RequiresApi(Build.VERSION_CODES.P)
class Biometrics {

    fun isBiometricHardWareAvailable(con: Context): Boolean {
        val result: Boolean
        val biometricManager = BiometricManager.from(con)

        result = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            when (biometricManager.canAuthenticate(BiometricManager.Authenticators.DEVICE_CREDENTIAL or BiometricManager.Authenticators.BIOMETRIC_STRONG)) {
                BiometricManager.BIOMETRIC_SUCCESS -> true
                else -> false
            }
        } else {
            when (biometricManager.canAuthenticate(BIOMETRIC_WEAK)) {
                BiometricManager.BIOMETRIC_SUCCESS -> true
                else -> false
            }
        }
        return result
    }

}