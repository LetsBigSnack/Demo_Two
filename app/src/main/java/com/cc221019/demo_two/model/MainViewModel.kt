package com.cc221019.demo_two.model

import androidx.lifecycle.ViewModel
import com.cc221019.demo_two.data.BccStudent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel: ViewModel() {
    private val _bccStudentState = MutableStateFlow(BccStudent("",""))
    val bccStudentState: StateFlow<BccStudent> = _bccStudentState.asStateFlow()

    fun save(student: BccStudent){
        _bccStudentState.value = student
    }
}