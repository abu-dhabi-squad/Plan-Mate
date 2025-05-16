package helper

import logic.model.Project
import logic.model.TaskState
import java.util.UUID

fun createProject(
    id: UUID = UUID.randomUUID(),
    name: String = "",
    taskStates : List<TaskState> = listOf()
):Project{
    return Project(
        id,
        name,
        taskStates
    )
}