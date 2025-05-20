package logic.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
data class Project(
    val projectId: Uuid = Uuid.random(),
    val projectName: String,
    val taskStates: List<TaskState>,
)

