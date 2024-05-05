package com.mongodb.tasktracker.model
import org.bson.types.ObjectId
import java.io.Serializable


data class IntructorInfo(
    val instructorId: ObjectId,
    val teacherName: String,
    val teacherEmail: String,

    ) : Serializable
