package squad.abudhabi.logic.model

import java.util.*

data class State(
    val id: String= UUID.randomUUID().toString(),
    val name: String
)
