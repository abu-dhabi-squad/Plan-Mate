package helper

import squad.abudhabi.logic.model.State
import java.util.UUID

fun createState(id: String = UUID.randomUUID().toString(), name: String= "state name"): State = State(id = id, name = name)