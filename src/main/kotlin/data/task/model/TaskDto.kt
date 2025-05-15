package data.task.model

import org.bson.codecs.pojo.annotations.BsonId

data class TaskDto(
    @BsonId val _id: String,
    val userName: String,
    val projectId: String,
    val stateId: String,
    val title: String,
    val description: String,
    val startDate: String,
    val endDate: String
)