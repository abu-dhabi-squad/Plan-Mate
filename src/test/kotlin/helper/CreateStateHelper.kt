package helper

import logic.model.State
import java.util.UUID

fun createState(id: UUID = UUID.randomUUID(), name: String= "state name"): State = State(id = id, name = name)