package squad.abudhabi.data.project.repository

import squad.abudhabi.data.utils.filehelper.FileHelper
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.repository.ProjectRepository
import java.io.File

class ProjectRepositoryImpl(
    private val fileHelper: FileHelper
) : ProjectRepository {

    override fun getProjects(): Result<List<Project>> {
        TODO("Not yet implemented")
    }

    override fun addProject(project: Project): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override fun editProject(project: Project): Result<Boolean> {
        TODO("Not yet implemented")
    }
}