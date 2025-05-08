package data.project.mapper

import data.project.model.ProjectDto
import data.project.model.StateDto
import logic.model.Project
import logic.model.State
import java.util.UUID


class ProjectMapper {
    fun mapDtoToProject(projectDto: ProjectDto): Project {
        return Project(
            id = UUID.fromString(projectDto.id),
            projectName = projectDto.projectName,
            states = projectDto.states.map { stateDto -> mapDtoToState(stateDto) }
        )
    }
    fun mapDtoToState(stateDto: StateDto): State {
        return State(
            id = stateDto.id,
            name = stateDto.name
        )
    }

    fun mapProjectToDto(project: Project): ProjectDto {
        return ProjectDto(
            id = project.id.toString(),
            projectName = project.projectName,
            states = project.states.map { state -> mapStateToDto(state) }
        )
    }

    fun mapStateToDto(state: State): StateDto {
        return StateDto(
            id = state.id,
            name = state.name
        )
    }

}