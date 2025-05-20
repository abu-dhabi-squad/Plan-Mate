package helper

import logic.model.Project
import logic.model.TaskState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun createProject(
    id: Uuid = Uuid.random(),
    name: String = "",
    taskStates : List<TaskState> = listOf()
):Project{
    return Project(
        id,
        name,
        taskStates
    )
}