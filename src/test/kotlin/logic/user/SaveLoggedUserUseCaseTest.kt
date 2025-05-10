package logic.user

import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import logic.exceptions.EmptyUsernameException
import logic.model.User
import logic.model.UserType
import logic.repository.AuthenticationRepository
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
