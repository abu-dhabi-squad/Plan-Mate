package logic.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
@OptIn(ExperimentalUuidApi::class)
data class TaskState(
    val stateId: Uuid = Uuid.random(),
    val stateName: String
)