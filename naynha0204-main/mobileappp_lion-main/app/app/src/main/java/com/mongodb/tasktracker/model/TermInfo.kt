package com.mongodb.tasktracker.model

import org.bson.types.ObjectId
import java.io.Serializable

data class TermInfo(
    val termId: ObjectId,
    val name: String,
    val startDate: Long,
    val endDate: Long,
    val departments: List<ObjectId>
) : Serializable