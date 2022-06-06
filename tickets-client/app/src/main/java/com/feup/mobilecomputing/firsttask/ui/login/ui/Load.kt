package com.feup.mobilecomputing.firsttask.ui.login.ui

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.feup.mobilecomputing.firsttask.R
import com.feup.mobilecomputing.firsttask.config.Config
import com.feup.mobilecomputing.firsttask.services.userSrv

class Load : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_load, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navController = findNavController()
        val sp = requireActivity().getSharedPreferences(Config.SP_NAME, Context.MODE_PRIVATE)
        if (userSrv(sp).getUserId().isEmpty()) {
            navController.navigate(R.id.register)
        } else {
            navController.navigate(R.id.login)
        }
    }
}