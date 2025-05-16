package helper

import logic.model.TaskState
import java.util.UUID

fun createState(id: UUID = UUID.randomUUID(), name: String= "state name"): TaskState = TaskState(stateId = id, stateName = name)