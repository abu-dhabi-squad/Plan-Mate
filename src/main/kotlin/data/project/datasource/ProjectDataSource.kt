package squad.abudhabi.data.project.datasource

import squad.abudhabi.logic.model.Project

interface ProjectDataSource {
    fun readProjects(): List<Project>
    fun writeProject(project: Project): Boolean
    fun editProject(project: Project): Boolean
    fun deleteProject(projectId: String): Boolean
    fun getProject(projectId: String): Project?
}