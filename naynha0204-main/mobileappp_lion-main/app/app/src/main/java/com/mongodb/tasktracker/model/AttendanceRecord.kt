package com.mongodb.tasktracker.model

import java.io.Serializable
import java.util.Date

data class AttendanceRecord(
    val slotId: String,
    val courseId: String,
    val courseTitle: String,
    val attendanceDate: String,
    val attendanceStatus: String,
    val attendanceTime: String
) : Serializable