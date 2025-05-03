package logic.user

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.data.authentication.datasource.LoggedUserDataSource
import squad.abudhabi.logic.exceptions.NoLoggedInUserException
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.model.UserType
import squad.abudhabi.logic.repository.AuthenticationRepository
import squad.abudhabi.logic.user.GetLoggedUserUseCase

class GetLoggedUserUseCaseTest{
    private lateinit var getLoggedUserUseCase: GetLoggedUserUseCase
    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setup(){
        authenticationRepository= mockk(relaxed = true)
        getLoggedUserUseCase= GetLoggedUserUseCase(authenticationRepository)
    }

    @Test
    fun `should return logged user when getLoggedUser is called`(){
        val user = User(
            username = "newUser",
            password = "ValidPass123!",
            userType = UserType.MATE
        )
        every{authenticationRepository.getLoggedUser()} returns  user
        val res=getLoggedUserUseCase()
        assertThat(res).isEqualTo(user)
        verify { authenticationRepository.getLoggedUser() }
    }
    @Test
    fun `should throw exception when no logged user is found`(){
        every{authenticationRepository.getLoggedUser()} returns null
        assertThrows<NoLoggedInUserException>{
            getLoggedUserUseCase()
        }
    }
}