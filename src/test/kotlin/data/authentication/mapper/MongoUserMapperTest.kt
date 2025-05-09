package data.authentication.mapper

import com.google.common.truth.Truth.assertThat
import data.authentication.model.UserDto
import logic.exceptions.UserTypeNotFoundException
import logic.model.User
import logic.model.UserType
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID
import kotlin.test.assertFailsWith

class MongoUserMapperTest {

    private lateinit var mapper: MongoUserMapper

    @BeforeEach
    fun setUp() {
        mapper = MongoUserMapper()
    }

    @Test
    fun `userToDto should convert User to UserDto correctly`() {
        // Given
        val user = User(
            id = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            username = "user1",
            password = "pass1",
            userType = UserType.ADMIN
        )

        // When
        val dto = mapper.userToDto(user)

        // Then
        assertThat(dto.id).isEqualTo(user.id.toString())
        assertThat(dto.username).isEqualTo(user.username)
        assertThat(dto.password).isEqualTo(user.password)
        assertThat(dto.userType).isEqualTo(user.userType.name)
    }

    @Test
    fun `dtoToUser should convert UserDto to User correctly`() {
        // Given
        val dto = UserDto(
            id = "550e8400-e29b-41d4-a716-446655440000",
            username = "user1",
            password = "pass1",
            userType = "ADMIN"
        )

        // When
        val user = mapper.dtoToUser(dto)

        // Then
        assertThat(user.id).isEqualTo(UUID.fromString(dto.id))
        assertThat(user.username).isEqualTo(dto.username)
        assertThat(user.password).isEqualTo(dto.password)
        assertThat(user.userType).isEqualTo(UserType.ADMIN)
    }

    @Test
    fun `dtoToUser should throw UserTypeNotFoundException for invalid userType`() {
        // Given
        val dto = UserDto(
            id = "550e8400-e29b-41d4-a716-446655440000",
            username = "user1",
            password = "pass1",
            userType = "INVALID_TYPE"
        )

        // Then
        assertFailsWith<UserTypeNotFoundException> {
            mapper.dtoToUser(dto)
        }
    }
}