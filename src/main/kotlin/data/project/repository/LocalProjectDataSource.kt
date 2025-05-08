package data.project.repository

import logic.model.Project

interface LocalProjectDataSource {
   fun getAllProjects(): List<Project>
   fun createProject(project: Project)
   fun editProject(project: Project)
   fun deleteProject(projectId: String)
   fun getProjectById(projectId: String): Project?
}

