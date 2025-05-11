package data.project.mapper

import data.project.model.ProjectDto
import data.project.model.StateDto
import logic.model.Project
import logic.model.TaskState
import java.util.*

class ProjectMapper {
    fun dtoToProject(projectDto: ProjectDto): Project {
        return Project(
            projectId = UUID.fromString(projectDto.id),
            projectName = projectDto.projectName,
            taskStates = projectDto.states.map { stateDto -> dtoToState(stateDto) }
        )
    }

    fun dtoToState(stateDto: StateDto): TaskState {
        return TaskState(
            stateId = UUID.fromString(stateDto.id),
            stateName = stateDto.name
        )
    }

    fun projectToDto(project: Project): ProjectDto {
        return ProjectDto(
            id = project.projectId.toString(),
            projectName = project.projectName,
            states = project.taskStates.map { state -> stateToDto(state) }
        )
    }

    fun stateToDto(taskState: TaskState): StateDto {
        return StateDto(
            id = taskState.stateId.toString(),
            name = taskState.stateName
        )
    }
}