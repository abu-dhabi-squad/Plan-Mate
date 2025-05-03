package logic.user

import com.google.common.base.Verify.verify
import io.mockk.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import squad.abudhabi.logic.exceptions.EmptyUsernameException
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.model.UserType
import squad.abudhabi.logic.repository.AuthenticationRepository
import squad.abudhabi.logic.user.SaveLoggedUserUseCase
import squad.abudhabi.logic.validation.PasswordValidator
import kotlin.test.Test

class SaveLoggedUserUseCaseTest{
    private lateinit var saveLoggedUserUseCase: SaveLoggedUserUseCase
    private lateinit var authenticationRepository: AuthenticationRepository
    private lateinit var passwordValidator: PasswordValidator

    @BeforeEach
    fun setup(){
        authenticationRepository= mockk(relaxed = true)
        passwordValidator = mockk(relaxed = true)
        saveLoggedUserUseCase=SaveLoggedUserUseCase(authenticationRepository,passwordValidator)
    }

    @Test
    fun `should save the logged user when inputs are valid`() {
        val name = "shahd"
        val pass = "pass123"
        val user = User(username=name, password=pass, userType = UserType.MATE)

        saveLoggedUserUseCase(user)

        verify { passwordValidator.validatePassword(user.password) } // Verify password validation was called
        verify { authenticationRepository.saveLoggedUser(user) } // Verify that the user is saved
    }

    @Test
    fun ` saveLoggedUserUseCase should call password validator`() {
        val user = User(
            username = "newUser",
            password = "ValidPass123!",
            userType = UserType.MATE
        )
        every { passwordValidator.validatePassword(any()) } just runs

        saveLoggedUserUseCase(user)

        verify { passwordValidator.validatePassword(user.password) }
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
