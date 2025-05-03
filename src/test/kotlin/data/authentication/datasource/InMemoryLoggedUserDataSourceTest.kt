package data.authentication.datasource

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import squad.abudhabi.data.authentication.datasource.InMemoryLoggedUserDataSource
import squad.abudhabi.logic.model.User
import squad.abudhabi.logic.model.UserType

class InMemoryLoggedUserDataSourceTest{
    private lateinit var inMemoryLoggedUserDataSource: InMemoryLoggedUserDataSource

    @BeforeEach
    fun setup(){
        inMemoryLoggedUserDataSource=InMemoryLoggedUserDataSource()
    }

    @Test
    fun `saveLoggedUser should save user correctly`(){
        val user = User(
            username = "",
            password = "ValidPass123!",
            userType = UserType.MATE
        )
        inMemoryLoggedUserDataSource.saveLoggedUser(user)

        assertThat(inMemoryLoggedUserDataSource.getLoggedUser()).isEqualTo(user)

    }

    @Test
    fun `getLoggedUser should return null when no user was logged`(){
        val res=inMemoryLoggedUserDataSource.getLoggedUser()
        assertThat(res).isNull()
    }
}