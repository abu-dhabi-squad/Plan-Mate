package helper

import logic.model.Project
import logic.model.State
import java.util.UUID

fun createProject(
    id: UUID = UUID.randomUUID(),
    name: String = "",
    states : List<State> = listOf()
):Project{
    return Project(
        id,
        name,
        states
    )
}