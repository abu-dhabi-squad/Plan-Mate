package squad.abudhabi.data.project.datasource

import squad.abudhabi.logic.model.Project

interface ProjectDataSource{

    fun readProjects(): List<Project>

    fun writeProjects(projects: List<Project>): Result<Boolean>
}