package com.example.proyectofinal.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterViewModel : ViewModel() {

    private val _datosParte1Validos = MutableLiveData(false)
    val datosParte1Validos: LiveData<Boolean> = _datosParte1Validos

    private val _datosParte2Validos = MutableLiveData(false)
    val datosParte2Validos: LiveData<Boolean> = _datosParte2Validos

    private val _datosParte3Validos = MutableLiveData(false)
    val datosParte3Validos: LiveData<Boolean> = _datosParte3Validos

    private val _showDatePicker = MutableLiveData<Boolean>()
    val showDatePicker: LiveData<Boolean>
        get() = _showDatePicker

    fun onBirthdayEditTextClicked() {
        _showDatePicker.value = true
    }

    fun onDatePickerDismissed() {
        _showDatePicker.value = false
    }

    fun guardarDatosParte1(nombre: String, fechaNacimiento: String) {
        // Aquí puedes implementar la lógica para guardar los datos de la primera parte del registro
    }

    fun guardarDatosParte2(email: String, pass: String) {
        // Aquí puedes implementar la lógica para guardar los datos de la segunda parte del registro
    }

    fun guardarDatosParte3() {
        // Aquí puedes implementar la lógica para guardar los datos de la tercera parte del registro
    }
}