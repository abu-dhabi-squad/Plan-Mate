package squad.abudhabi.logic.model

import java.time.LocalDate
import java.util.UUID

data class Task(
    val id: String = UUID.randomUUID().toString(),
    val userId: String,
    val projectId: String,
    val stateId: String,
    val title: String,
    val description: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
)
