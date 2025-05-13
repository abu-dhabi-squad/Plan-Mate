package logic.model

import java.util.UUID


data class Project(
    val projectId: UUID = UUID.randomUUID(),
    val projectName: String,
    val taskStates: List<TaskState>,
)

