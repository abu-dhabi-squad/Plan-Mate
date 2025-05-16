package logic.model

import java.time.LocalDate
import java.util.UUID

data class Task(
    val taskId: UUID = UUID.randomUUID(),
    val username: String,
    val projectId: UUID,
    val taskStateId: UUID,
    val title: String,
    val description: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
)
