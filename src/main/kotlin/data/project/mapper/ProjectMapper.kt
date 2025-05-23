package data.project.mapper

import data.project.model.ProjectDto
import data.project.model.StateDto
import logic.model.Project
import logic.model.TaskState
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
class ProjectMapper {
    fun dtoToProject(projectDto: ProjectDto): Project {
        return Project(
            projectId = Uuid.parse(projectDto._id),
            projectName = projectDto.projectName,
            taskStates = projectDto.states.map { stateDto -> dtoToState(stateDto) }
        )
    }

    fun dtoToState(stateDto: StateDto): TaskState {
        return TaskState(
            stateId = Uuid.parse(stateDto._id),
            stateName = stateDto.name
        )
    }

    fun projectToDto(project: Project): ProjectDto {
        return ProjectDto(
            _id = project.projectId.toString(),
            projectName = project.projectName,
            states = project.taskStates.map { state -> stateToDto(state) }
        )
    }

    fun stateToDto(taskState: TaskState): StateDto {
        return StateDto(
            _id = taskState.stateId.toString(),
            name = taskState.stateName
        )
    }
}