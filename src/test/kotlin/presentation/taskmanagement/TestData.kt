package presentation.taskmanagement

import logic.model.Project
import logic.model.TaskState
import logic.model.User
import logic.model.UserType
import java.time.LocalDate
import java.util.UUID

object TestData {
    val fixedUserId = UUID.fromString("11111111-1111-1111-1111-111111111111")
    val fakeUser = User(fixedUserId, "noor", "pass", UserType.MATE)

    val fixedProjectId = UUID.fromString("22222222-2222-2222-2222-222222222222")
    val fixedStateId = UUID.fromString("33333333-3333-3333-3333-333333333333")
    val fakeDate = LocalDate.of(2025, 5, 12)

    val fakeProject = Project(
        projectId = fixedProjectId,
        projectName = "Project A",
        taskStates = listOf(TaskState(fixedStateId, "To Do"))
    )
}