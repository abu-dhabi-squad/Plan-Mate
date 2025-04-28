package squad.abudhabi.logic.repository

import squad.abudhabi.logic.model.Project

interface ProjectRepository {
    fun getProjects() : Result<List<Project>>
    fun addProject(project: Project) : Result<Boolean>
    fun editProject(project: Project) : Result<Boolean>
}