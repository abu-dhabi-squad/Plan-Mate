package data.project.mapper

import data.project.model.ProjectDto
import data.project.model.StateDto
import logic.model.Project
import logic.model.State
import java.util.UUID

class MongoProjectMapper {
    fun dtoToProject(projectDto: ProjectDto): Project {
        return Project(
            id = UUID.fromString(projectDto.id),
            projectName = projectDto.projectName,
            states = projectDto.states.map { stateDto -> dtoToState(stateDto) }
        )
    }
    fun dtoToState(stateDto: StateDto): State {
        return State(
            id = UUID.fromString(stateDto.id),
            name = stateDto.name
        )
    }

    fun projectToDto(project: Project): ProjectDto {
        return ProjectDto(
            id = project.id.toString(),
            projectName = project.projectName,
            states = project.states.map { state -> stateToDto(state) }
        )
    }

    fun stateToDto(state: State): StateDto {
        return StateDto(
            id = state.id.toString(),
            name = state.name
        )
    }
}