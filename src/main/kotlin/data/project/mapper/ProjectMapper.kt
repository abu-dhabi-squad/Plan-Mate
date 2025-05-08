package data.project.mapper

import data.project.model.ProjectDto
import data.project.model.StateDto
import logic.model.Project
import logic.model.State
import java.util.UUID


class ProjectMapper {
    fun projectDtoToProject(projectDto: ProjectDto): Project {
        return Project(
            id = UUID.fromString(projectDto.id),
            projectName = projectDto.projectName,
            states = projectDto.states.map { stateDto -> stateDtoToState(stateDto) }
        )
    }
    fun stateDtoToState(stateDto: StateDto): State {
        return State(
            id = stateDto.id,
            name = stateDto.name
        )
    }

    fun projectToProjectDto(project: Project): ProjectDto {
        return ProjectDto(
            id = project.id.toString(),
            projectName = project.projectName,
            states = project.states.map { state -> stateToStateDto(state) }
        )
    }

    fun stateToStateDto(state: State): StateDto {
        return StateDto(
            id = state.id,
            name = state.name
        )
    }

}