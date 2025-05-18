package logic.model

import java.time.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
@OptIn(ExperimentalUuidApi::class)
data class Task(
    val taskId: Uuid = Uuid.random(),
    val username: String,
    val projectId: Uuid,
    val taskStateId: Uuid,
    val title: String,
    val description: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
)
