package logic.model

import java.util.UUID

data class TaskState(
    val stateId: UUID = UUID.randomUUID(),
    val stateName: String
)