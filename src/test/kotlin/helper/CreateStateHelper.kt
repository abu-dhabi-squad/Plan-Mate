package helper

import logic.model.TaskState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun createState(id: Uuid = Uuid.random(), name: String= "state name"): TaskState = TaskState(stateId = id, stateName = name)