package com.test.employeepresence.ui.settings

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.employeepresence.places.domain.WorkingPlace
import com.test.employeepresence.places.domain.PlacesInteractor
import com.test.employeepresence.utils.APP_LOGTAG
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val interactor: PlacesInteractor) : ViewModel() {
    private val _placeLiveData = MutableLiveData<WorkingPlace>()
    val placeLiveData: LiveData<WorkingPlace> = _placeLiveData

    init {
        viewModelScope.launch {
            interactor.placesFlow.collect {
                Log.d(APP_LOGTAG, "Place updated: $it")
                it?.let {
                    _placeLiveData.value = it
                    interactor.setupGeofence(it)
                }
            }
        }
    }

    fun requestCurrentLocation() {
        viewModelScope.launch {
            interactor.requestCurrentLocation()
        }
    }
}