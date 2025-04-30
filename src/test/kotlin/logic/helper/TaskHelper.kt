package logic.helper

import squad.abudhabi.logic.model.Task
import java.time.LocalDate
import java.util.UUID

fun createTask(id: String = UUID.randomUUID().toString()
               ,userId:String = "",
               projectId:String = "",
               stateId:String = "",
               title:String = "",
               description:String = "",
               startDate:LocalDate = LocalDate.now(),
               endTime:LocalDate = LocalDate.now().plusDays(7)
): Task {
    return Task(
        id = id,
        userName = userId,
        projectId = projectId,
        stateId = stateId,
        title = title,
        description = description,
        startDate = startDate,
        endDate = endTime
    )
}