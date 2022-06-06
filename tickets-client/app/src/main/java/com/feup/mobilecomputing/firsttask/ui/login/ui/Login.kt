package com.feup.mobilecomputing.firsttask.ui.login.ui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.feup.mobilecomputing.firsttask.R
import com.feup.mobilecomputing.firsttask.config.Config
import com.feup.mobilecomputing.firsttask.databinding.ActivityMainBinding
import com.feup.mobilecomputing.firsttask.services.userSrv
import com.feup.mobilecomputing.firsttask.ui.NavBar
import com.feup.mobilecomputing.firsttask.ui.utils.Biometrics
import java.util.concurrent.Executor

class Login : Fragment() {

    private val executor: Executor by lazy { ContextCompat.getMainExecutor(requireContext()) }
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            setPrompt()
            if (Biometrics().isBiometricHardWareAvailable(requireContext())) {
                initBiometricPrompt(
                    "Biometric Authentication",
                    "Use your your fingerprint to login",
                    "This app makes use of the biometrics to authenticate.",
                    false
                )
            } else {
                initBiometricPrompt(
                    "Biometric Authentication",
                    "Use your your fingerprint to login",
                    "This app makes use of the biometrics to authenticate.",
                    true
                )
            }
            biometricPrompt.authenticate(promptInfo)
        }
        val pinText = view.findViewById<EditText>(R.id.pin_login)
        view.findViewById<Button>(R.id.button_login).setOnClickListener {
            if (userSrv(requireActivity().getSharedPreferences(Config.SP_NAME, Context.MODE_PRIVATE)).comparePINs(pinText.text.toString())) {
                val intent = Intent(requireContext(), NavBar::class.java)
                startActivity(intent)
            }
            else {
                Toast.makeText(context, "Authentication error", Toast.LENGTH_LONG).show()
                pinText.error = "PIN incorrecto"
            }
        }

        val fingerprintButton = view.findViewById<ImageButton>(R.id.fingerprint_button)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && Biometrics().isBiometricHardWareAvailable(requireContext())) {
                fingerprintButton.setOnClickListener {
                    biometricPrompt.authenticate(promptInfo)
                }
        } else {
            fingerprintButton.visibility = View.GONE
        }

    }

    private fun setPrompt() {
        biometricPrompt = BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(context, "Authentication error", Toast.LENGTH_LONG).show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    val intent = Intent(requireContext(), NavBar::class.java)
                    startActivity(intent)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(context, "Authentication failed", Toast.LENGTH_LONG).show()
                }
            })
    }

    @SuppressLint("WrongConstant")
    private fun initBiometricPrompt(
        title: String,
        subtitle: String,
        description: String,
        setDeviceCred: Boolean
    ) {
        if (setDeviceCred) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val authFlag = DEVICE_CREDENTIAL or BIOMETRIC_STRONG
                promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle(title)
                    .setSubtitle(subtitle)
                    .setDescription(description)
                    .setAllowedAuthenticators(authFlag)
                    .build()
            } else {
                promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle(title)
                    .setSubtitle(subtitle)
                    .setDescription(description)
                    .setDeviceCredentialAllowed(true)
                    .build()
            }
        } else {
            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(title)
                .setSubtitle(subtitle)
                .setDescription(description)
                .setNegativeButtonText("Cancel")
                .build()
        }

    }

}