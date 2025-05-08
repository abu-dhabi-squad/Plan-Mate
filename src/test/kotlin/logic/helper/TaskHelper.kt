package logic.helper

import logic.model.Task
import java.time.LocalDate
import java.util.UUID

fun createTask(
    id: String = UUID.randomUUID().toString(),
    userName: String = "",
    projectId: String = "",
    stateId: String = "",
    title: String = "",
    description: String = "",
    startDate: LocalDate = LocalDate.now(),
    endDate: LocalDate = LocalDate.now().plusDays(7)
): Task {
    return Task(
        id = id,
        userName = userName,
        projectId = projectId,
        stateId = stateId,
        title = title,
        description = description,
        startDate = startDate,
        endDate = endDate
    )
}