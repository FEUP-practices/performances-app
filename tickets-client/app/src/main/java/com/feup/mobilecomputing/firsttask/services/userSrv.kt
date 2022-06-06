package com.feup.mobilecomputing.firsttask.services

import android.content.SharedPreferences

class userSrv(private val sp: SharedPreferences) {

    companion object {
        const val KEY_USER_ID = "userId"
        const val KEY_PIN = "pin"
    }

    private fun getPIN(): String{
        return sp.getString(KEY_PIN, "") ?: ""
    }

    fun setPIN(pin: String){
        with(sp.edit()){
            putString(KEY_PIN, pin)
            apply()
        }
    }

    fun comparePINs(inputPin: String): Boolean{
        return inputPin == getPIN()
    }

    fun getUserId(): String {
        return sp.getString(KEY_USER_ID, "") ?: ""
    }

    fun setUserId(userId: String) {
        with(sp.edit()){
            putString(KEY_USER_ID, userId)
            apply()
        }
    }
}