package helper

import logic.model.Project
import logic.model.State
import java.util.UUID

fun createProject(
    id: String = UUID.randomUUID().toString(),
    name: String = "project name",
    states: List<State> = listOf()
): Project = Project(id = id, projectName = name, states = states)