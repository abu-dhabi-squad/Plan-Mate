package logic.project

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.exceptions.NoProjectsFoundException
import squad.abudhabi.logic.exceptions.ProjectNotFoundException
import squad.abudhabi.logic.model.Project
import squad.abudhabi.logic.project.GetProjectByIdUseCase
import squad.abudhabi.logic.repository.ProjectRepository
import kotlin.test.Test


class GetProjectByIdUseCaseTest