package logic.model

import java.util.*

data class Project(
    val id: String= UUID.randomUUID().toString(),
    val projectName: String,
    val states: List<State>,

    )

