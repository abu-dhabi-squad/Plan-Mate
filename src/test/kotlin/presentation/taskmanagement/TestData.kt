package presentation.taskmanagement

import logic.model.Project
import logic.model.Task
import logic.model.TaskState
import logic.model.User
import logic.model.User.UserType
import kotlinx.datetime.LocalDate
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
object TestData {

    val fakeUser = User(
        Uuid.parse("11111111-1111-1111-1111-111111111111"),
        "noor",
        "pass",
        UserType.MATE)

    val fakeDate = LocalDate(2025, 5, 12)
    val fakeDate2 = LocalDate(2025, 5, 13)

    val testState = TaskState(
        stateId = Uuid.parse("55555555-5555-5555-5555-555555555555"),
        stateName = "To Do"
    )

    val fakeProject = Project(
        projectId = Uuid.parse("22222222-2222-2222-2222-222222222222"),
        projectName = "Project A",
        taskStates = listOf(testState)

    )

    val fakeTask = Task(
        taskId = Uuid.parse("44444444-4444-4444-4444-444444444444"),
        username = "noor",
        title = "Test Task",
        description = "desc",
        startDate = fakeDate,
        endDate = fakeDate,
        projectId = fakeProject.projectId,
        taskStateId = testState.stateId
    )
}