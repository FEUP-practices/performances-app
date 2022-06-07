package com.feup.mobilecomputing.firsttask.ui.login.ui

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.feup.mobilecomputing.firsttask.MainActivity
import com.feup.mobilecomputing.firsttask.R
import com.feup.mobilecomputing.firsttask.api.CallbackAPI
import com.feup.mobilecomputing.firsttask.api.UsersAPI
import com.feup.mobilecomputing.firsttask.config.Config
import com.feup.mobilecomputing.firsttask.middleware.SecureStore
import com.feup.mobilecomputing.firsttask.models.SignatureType
import com.feup.mobilecomputing.firsttask.models.UserType
import com.feup.mobilecomputing.firsttask.services.userSrv
import com.feup.mobilecomputing.firsttask.ui.NavBar
import com.google.android.material.navigation.NavigationView


const val ERR_EMPTY_INPUT = "You must fill this field"
const val ERR_PIN_INPUT = "You must fill it with 4 numbers"
const val ERR_BAD_DATE_INPUT = "Date must be in MM/YYYY format"

class Register : Fragment() {

    private lateinit var buttonRegister: Button
    private lateinit var nameText: EditText
    private lateinit var nifText: EditText
    private lateinit var cardNumberText: EditText
    private lateinit var cvvNumberText: EditText
    private lateinit var cardType: Spinner
    private lateinit var expirationDateText: EditText
    private lateinit var pinText: EditText

    private lateinit var sp : SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) : View = inflater.inflate(R.layout.fragment_register, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //view.rootView.findViewById<NavigationView>(R.id.nav_view).visibility = View.GONE
        loadFields(view)
        val challenge = SecureStore().genRandomChallenge()
        buttonRegister.setOnClickListener {
            if (checkFields()){
                SecureStore().generateAndStoreKeys(requireContext())
                UsersAPI(requireContext()).registerUser(UserType(
                    nif = nifText.text.toString(),
                    name = nameText.text.toString(),
                    cardNumber = cardNumberText.text.toString(),
                    cardCVV = cvvNumberText.text.toString(),
                    cardValidity = expirationDateText.text.toString(),
                    cardType = cardType.selectedItem.toString(),
                ), SignatureType(
                    challenge = Base64.encodeToString(challenge, Base64.DEFAULT),
                    signature = SecureStore().signContent(challenge) ?: ""
                ), Base64.encodeToString(SecureStore().getPubKey().encoded, Base64.DEFAULT), object : CallbackAPI<String> {
                    override fun onSuccess(response: String) {
                        userSrv(sp).setUserId(response)
                        userSrv(sp).setPIN(pinText.text.toString())
                        val intent = Intent(requireContext(), NavBar::class.java)
                        startActivity(intent)
                    }
                    override fun onError(errorMsg: String?) {
                        Toast.makeText(requireContext(), "Registration failed", Toast.LENGTH_LONG).show()
                    }
                }
                )
            }
        }
    }

    private fun loadFields(view: View){
        sp = requireActivity().getSharedPreferences(Config.SP_NAME, Context.MODE_PRIVATE)
        with(view) {
            cardType = findViewById(R.id.card_selector_regis)
            val dayValues = resources.getStringArray(R.array.card_types)
            val adapter: ArrayAdapter<String?> = object : ArrayAdapter<String?>(
                requireContext(),
                android.R.layout.simple_spinner_item, dayValues
            ) {}
            cardType.adapter = adapter

            buttonRegister = findViewById(R.id.button_regis)
            nameText = findViewById(R.id.name_regis)
            nifText = findViewById(R.id.nif_regis)
            cardNumberText = findViewById(R.id.card_number_regis)
            cvvNumberText = findViewById(R.id.CVV_card_regis)
            expirationDateText = findViewById(R.id.exp_date_regis)
            pinText = findViewById(R.id.pin_regis)
        }
    }

    private fun checkFields(): Boolean{
        if (nameText.text.isNullOrEmpty()) {
            nameText.error = ERR_EMPTY_INPUT
            return false
        }
        if (nifText.text.isNullOrEmpty()) {
            nifText.error = ERR_EMPTY_INPUT
            return false
        }
        if (cardNumberText.text.isNullOrEmpty()) {
            cardNumberText.error = ERR_EMPTY_INPUT
            return false
        }
        if (cvvNumberText.text.isNullOrEmpty()) {
            cvvNumberText.error = ERR_EMPTY_INPUT
            return false
        }
        if (expirationDateText.text.isNullOrEmpty()) {
            expirationDateText.error = ERR_EMPTY_INPUT
            return false
        }
        if (!expirationDateText.text.matches(Regex("(0[1-9]|10|11|12)/20[0-9]{2}\$"))) {
            expirationDateText.error = ERR_BAD_DATE_INPUT
            return false
        }
        if (pinText.text.isNullOrEmpty() || pinText.text.length != 4) {
            pinText.error = ERR_PIN_INPUT
            return false
        }
        return true
    }

}