package com.mongodb.tasktracker.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    val attendanceHistory = MutableLiveData<MutableList<AttendanceRecord>>(mutableListOf())

    fun addAttendanceRecord(record: AttendanceRecord) {
        val currentList = attendanceHistory.value ?: mutableListOf()
        currentList.add(record)
        attendanceHistory.value = currentList // Trigger LiveData update
    }
}