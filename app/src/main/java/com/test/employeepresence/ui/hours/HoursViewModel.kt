package com.test.employeepresence.ui.hours

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.employeepresence.hours.domain.DayRecord
import com.test.employeepresence.hours.domain.HoursInteractor
import com.test.employeepresence.places.domain.PlacesInteractor
import com.test.employeepresence.utils.APP_LOGTAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HoursViewModel @Inject constructor(private val interactor: HoursInteractor) : ViewModel() {

    private val _hours = MutableLiveData<List<DayRecord>>()
    val hours: LiveData<List<DayRecord>> = _hours

    fun retrieveHours() {
        viewModelScope.launch {
            val dayRecords = interactor.getHours()
            Log.d(APP_LOGTAG, "viewmodel getHours $dayRecords")
            _hours.value = dayRecords
        }
    }
}