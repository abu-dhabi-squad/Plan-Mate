package squad.abudhabi.data.project.datasource

import squad.abudhabi.data.utils.filehelper.FileHelper
import squad.abudhabi.logic.model.Project


class CsvProjectDataSource(
    private val fileHelper: FileHelper
) : ProjectDataSource{
    override fun readProjects(): List<Project> {
        TODO("Not yet implemented")
    }

    override fun writeProjects(projects: List<Project>): Boolean {
        TODO("Not yet implemented")
    }

}