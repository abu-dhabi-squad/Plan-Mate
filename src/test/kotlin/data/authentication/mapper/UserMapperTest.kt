package data.authentication.mapper

import com.google.common.truth.Truth.assertThat
import data.authentication.model.UserDto
import logic.exceptions.UserTypeNotFoundException
import logic.model.User
import logic.model.UserType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.util.UUID

class UserMapperTest {

    private lateinit var mapper: UserMapper

    @BeforeEach
    fun setUp() {
        mapper = UserMapper()
    }

    @Test
    fun `userToDto should convert User to UserDto correctly`() {
        // Given
        val user = User(
            userId = UUID.fromString("550e8400-e29b-41d4-a716-446655440000"),
            username = "shahd",
            password = "pass1",
            userType = UserType.ADMIN
        )

        // When
        val dto = mapper.userToDto(user)

        // Then
        assertThat(dto._id).isEqualTo(user.userId.toString())
        assertThat(dto.username).isEqualTo(user.username)
        assertThat(dto.password).isEqualTo(user.password)
        assertThat(dto.userType).isEqualTo(user.userType.name)
    }

    @Test
    fun `dtoToUser should convert UserDto to User correctly`() {
        // Given
        val dto = UserDto(
            _id = "550e8400-e29b-41d4-a716-446655440000",
            username = "shahd",
            password = "pass1",
            userType = "ADMIN"
        )

        // When
        val user = mapper.dtoToUser(dto)

        // Then
        assertThat(user.userId).isEqualTo(UUID.fromString(dto._id))
        assertThat(user.username).isEqualTo(dto.username)
        assertThat(user.password).isEqualTo(dto.password)
        assertThat(user.userType).isEqualTo(UserType.ADMIN)
    }

    @Test
    fun `dtoToUser should throw UserTypeNotFoundException for invalid userType`() {
        // Given
        val dto = UserDto(
            _id = "550e8400-e29b-41d4-a716-446655440000",
            username = "shahd",
            password = "pass1",
            userType = "INVALID_TYPE"
        )

        // Then
        assertThrows<UserTypeNotFoundException> {
            mapper.dtoToUser(dto)
        }
    }
}