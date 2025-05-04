package logic.user

import io.mockk.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.exceptions.EmptyUsernameException
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.model.UserType
import squad.abudhabi.logic.repository.AuthenticationRepository
import squad.abudhabi.logic.user.SaveLoggedUserUseCase
import kotlin.test.Test

class SaveLoggedUserUseCaseTest{
    private lateinit var saveLoggedUserUseCase: SaveLoggedUserUseCase
    private lateinit var authenticationRepository: AuthenticationRepository

    @BeforeEach
    fun setup(){
        authenticationRepository= mockk(relaxed = true)
        saveLoggedUserUseCase=SaveLoggedUserUseCase(authenticationRepository)
    }


    @Test
    fun `should throw EmptyUserUserException when username is empty`(){
        val user = User(
            username = "",
            password = "ValidPass123!",
            userType = UserType.MATE
        )
       assertThrows<EmptyUsernameException>{
           saveLoggedUserUseCase(user)
       }

    }



}
