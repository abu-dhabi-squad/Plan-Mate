package squad.abudhabi.logic.model

import java.time.LocalDate

data class Task(
    val id: String,
    val userId: String,
    val projectId: String,
    val stateId: String,
    val title: String,
    val description: String,
    val startDate: LocalDate = LocalDate.now(),
    val endTime: LocalDate
)