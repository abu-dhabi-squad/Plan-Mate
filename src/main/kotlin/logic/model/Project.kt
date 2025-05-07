package logic.model

import java.util.UUID


data class Project(
    val id: UUID = UUID.randomUUID(),
    val projectName: String,
    val states: List<State>,
)

