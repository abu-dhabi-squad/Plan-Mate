package squad.abudhabi.data.project.datasource

import squad.abudhabi.logic.model.Project

interface ProjectDataSource {
    fun readProjects(): List<Project>
    fun writeProject(project: Project)
    fun editProject(project: Project)
    fun deleteProject(projectId: String)
    fun getProject(projectId: String): Project?
}