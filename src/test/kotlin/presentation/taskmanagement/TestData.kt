package presentation.taskmanagement

import logic.model.Project
import logic.model.Task
import logic.model.TaskState
import logic.model.User
import logic.model.UserType
import java.time.LocalDate
import java.util.UUID

object TestData {

    val fakeUser = User(
        UUID.fromString("11111111-1111-1111-1111-111111111111"),
        "noor",
        "pass",
        UserType.MATE)

    val fakeDate = LocalDate.of(2025, 5, 12)

    val fakeProject = Project(
        projectId = UUID.fromString("22222222-2222-2222-2222-222222222222"),
        projectName = "Project A",
        taskStates = listOf(
            TaskState(
                UUID.fromString("33333333-3333-3333-3333-333333333333"),
                "To Do"))
    )

    val fakeTask = Task(
        taskId = UUID.fromString("44444444-4444-4444-4444-444444444444"),
        username = "noor",
        title = "Test Task",
        description = "desc",
        startDate = LocalDate.of(2025, 5, 12),
        endDate = LocalDate.of(2025, 5, 12),
        projectId = fakeProject.projectId,
        taskStateId = UUID.randomUUID()
    )
}