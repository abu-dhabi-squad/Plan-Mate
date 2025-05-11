package logic.model

import java.util.*

data class TaskState(
    val stateId: UUID = UUID.randomUUID(),
    val stateName: String
)