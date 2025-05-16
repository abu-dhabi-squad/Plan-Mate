package data.authentication.datasource

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import data.authentication.datasource.inmemory.InMemoryLoggedUser
import logic.model.User
import logic.model.User.UserType

class InMemoryLoggedUserTest{
    private lateinit var inMemoryLoggedUser: InMemoryLoggedUser

    @BeforeEach
    fun setup(){
        inMemoryLoggedUser=InMemoryLoggedUser()
    }

    @Test
    fun `saveLoggedUser should save user correctly`(){
        val user = User(
            username = "shahd",
            password = "ValidPass123!",
            userType = UserType.MATE
        )
        inMemoryLoggedUser.saveLoggedUser(user)

        assertThat(inMemoryLoggedUser.getLoggedUser()).isEqualTo(user)

    }

    @Test
    fun `getLoggedUser should return null when no user was logged`(){
        val res=inMemoryLoggedUser.getLoggedUser()
        assertThat(res).isNull()
    }
}