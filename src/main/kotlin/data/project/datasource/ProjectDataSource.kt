package squad.abudhabi.data.project.datasource

import squad.abudhabi.logic.model.Project

interface ProjectDataSource{

    fun readProjects(): List<Project>

    fun writeProjects(projects: List<Project>): Boolean

    fun writeProject(project: Project): Boolean

    fun editProject(project: Project): Boolean

    fun deleteProject(project: Project): Boolean
}