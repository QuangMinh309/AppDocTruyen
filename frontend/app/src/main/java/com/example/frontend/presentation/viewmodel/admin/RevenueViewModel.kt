package com.example.frontend.presentation.viewmodel.admin

import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.viewModelScope
import com.example.frontend.data.model.DayRevenue
import com.example.frontend.data.repository.AdminRepository
import com.example.frontend.presentation.viewmodel.BaseViewModel
import com.example.frontend.services.navigation.NavigationManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class RevenueViewModel @Inject constructor(
    navigationManager: NavigationManager,
    private val adminRepository: AdminRepository
) : BaseViewModel(navigationManager) {
    private val _selectedYear = MutableStateFlow<String>("")
    val selectedYear : MutableStateFlow<String> = _selectedYear

    private val _selectedMonth = MutableStateFlow<String>("")
    val selectedMonth : MutableStateFlow<String> = _selectedMonth

    private val _revenueData = MutableStateFlow<List<DayRevenue>>(emptyList())
    val revenueData : MutableStateFlow<List<DayRevenue>> = _revenueData

    fun onUpdateMonth(month: String) {
        _selectedMonth.value = month
    }

    fun onUpdateYear(year: String) {
        _selectedYear.value = year
    }

    fun fetchData()
    {
        if(_selectedYear.value.isBlank() || _selectedMonth.value.isBlank() || !_selectedYear.value.isDigitsOnly() || !_selectedMonth.value.isDigitsOnly())
        {
            _toast.value = "Invalid input"
            return
        }
        if(_selectedYear.value > LocalDateTime.now().year.toString() || _selectedYear.value.toInt() < 0)
        {
            _toast.value = "Invalid year input"
            return
        }
        if(_selectedMonth.value.toInt() > 12 || _selectedMonth.value.toInt() < 0)
        {
            _toast.value = "Invalid month input"
            return
        }
        if(_selectedYear.value.toInt() <= 2000)
        {
            _toast.value = "Reports prior to 2001 aren't available"
            return
        }
        viewModelScope.launch {
            try {
                val result = adminRepository.getReport(_selectedYear.value.toInt(), _selectedMonth.value.toInt())
                result.onSuccess { list ->
                    _revenueData.value = list
                }
                result.onFailure { error ->
                    _toast.value = "Error: ${error.message}"
                    Log.e("apiError", "Error: ${error.message}")
                }
            } catch (e: Exception) {
                _toast.value = "Fetch Error: ${e.message}"
                Log.e("apiError", "FError: ${e.message}")
            }
        }
    }
}