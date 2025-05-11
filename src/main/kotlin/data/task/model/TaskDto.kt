package data.task.model

import org.bson.codecs.pojo.annotations.BsonProperty

data class TaskDto(
    val id: String,
    @BsonProperty("userName")
    val userName: String,

    @BsonProperty("projectId")
    val projectId: String,

    @BsonProperty("stateId")
    val stateId: String,

    @BsonProperty("title")
    val title: String,

    @BsonProperty("description")
    val description: String,

    @BsonProperty("startDate")
    val startDate: String,

    @BsonProperty("endDate")
    val endDate: String
)