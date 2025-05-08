package data.task.model

import org.bson.codecs.pojo.annotations.BsonProperty
import java.time.LocalDate
import java.util.UUID

data class Task(
    val id: UUID = UUID.randomUUID(),
    val userName: String,
    val projectId: String,
    val stateId: String,
    val title: String,
    val description: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
)

data class TaskDto(
    val id: String ,
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