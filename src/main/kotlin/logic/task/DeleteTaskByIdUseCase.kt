package squad.abudhabi.logic.task

import squad.abudhabi.logic.exceptions.TaskNotFoundException
import squad.abudhabi.logic.repository.TaskRepository

class DeleteTaskByIdUseCase (
    private val taskRepository: TaskRepository
){

    operator fun invoke(taskId:String){
        getTaskById(taskId) ?: throw TaskNotFoundException()
        taskRepository.deleteTask(taskId)
    }

    private fun getTaskById(taskId: String) =
        taskRepository.getAllTasks().find { it.id == taskId }

}