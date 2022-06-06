package com.feup.mobilecomputing.firsttask.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.feup.mobilecomputing.firsttask.models.PerformanceType

class HomeViewModel : ViewModel() {

    private val _performancesList = MutableLiveData<Array<PerformanceType>>().apply {
        value = arrayOf()
    }
    val performanceList: MutableLiveData<Array<PerformanceType>> = _performancesList
}