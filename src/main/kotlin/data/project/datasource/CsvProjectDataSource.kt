package squad.abudhabi.data.project.datasource

import squad.abudhabi.data.utils.filehelper.FileHelper
import squad.abudhabi.logic.model.Project
import java.io.File


class CsvProjectDataSource(
    private val fileHelper: FileHelper
): ProjectDataSource
{
    override fun readProjects(): List<Project> {
//        return fileHelper.readFile<List<String>>(File(PROJECTS_FILE_NAME))
//               .map {  }
        TODO("Not yet implemented")
    }

    override fun writeProjects(projects: List<Project>): Result<Boolean> {
        TODO("Not yet implemented")
    }

    companion object{
        const val  PROJECTS_FILE_NAME = "projects.csv"
    }
}