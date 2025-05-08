package data.authentication.mapper

import com.google.common.truth.Truth.assertThat
import data.authentication.model.UserDto
import logic.model.User
import logic.model.UserType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.UUID

class UserMapperTest {

    private lateinit var userMapper: UserMapper

    @BeforeEach
    fun setup() {
        userMapper = UserMapper()
    }

    @Test
    fun `userToDto should return UserDto with same field values`() {
        // Given
        val user = User(
            id = UUID.randomUUID().toString(),
            username = "noor123",
            password = "hashedPassword123",
            userType = UserType.ADMIN
        )

        // When
        val dto = userMapper.userToDto(user)

        // Then
        assertThat(dto.id).isEqualTo(user.id)
        assertThat(dto.username).isEqualTo(user.username)
        assertThat(dto.password).isEqualTo(user.password)
        assertThat(dto.userType).isEqualTo(user.userType.name)
    }

    @Test
    fun `dtoToUser should return User with same field values`() {
        // Given
        val dto = UserDto(
            id = UUID.randomUUID().toString(),
            username = "mate_user",
            password = "secure123",
            userType = "MATE"
        )

        // When
        val user = userMapper.dtoToUser(dto)

        // Then
        assertThat(user.id).isEqualTo(dto.id)
        assertThat(user.username).isEqualTo(dto.username)
        assertThat(user.password).isEqualTo(dto.password)
        assertThat(user.userType).isEqualTo(UserType.MATE)
    }

    @Test
    fun `dtoToUser should throw IllegalArgumentException for invalid userType`() {
        // Given
        val invalidDto = UserDto(
            id = UUID.randomUUID().toString(),
            username = "broken",
            password = "invalid123",
            userType = "NOT_A_REAL_TYPE"
        )

        // When & Then
        val exception = kotlin.runCatching {
            userMapper.dtoToUser(invalidDto)
        }.exceptionOrNull()

        assertThat(exception).isInstanceOf(IllegalArgumentException::class.java)
    }

}