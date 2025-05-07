package data.authentication.mapper

import com.google.common.truth.Truth.assertThat
import logic.model.User
import logic.model.UserType
import org.bson.Document
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UserMapperTest {
    private lateinit var userMapper: UserMapper

    @BeforeEach
    fun setup() {
        userMapper = UserMapper()
    }

    private val sampleUser = User(
        id = "123e4567-e89b-12d3-a456-426614174000",
        username = "testuser",
        password = "securepassword",
        userType = UserType.ADMIN
    )

    @Test
    fun `should convert User to Document correctly`() {
        // when
        val document = userMapper.userToDocument(sampleUser)

        // then
        assertThat(document.getString("id")).isEqualTo(sampleUser.id)
        assertThat(document.getString("username")).isEqualTo(sampleUser.username)
        assertThat(document.getString("password")).isEqualTo(sampleUser.password)
        assertThat(document.getString("userType")).isEqualTo(sampleUser.userType.name)
    }

    @Test
    fun `should convert Document to User correctly`() {
        // given
        val document = Document().apply {
            append("id", "123e4567-e89b-12d3-a456-426614174000")
            append("username", "testuser")
            append("password", "securepassword")
            append("userType", "ADMIN")
        }

        // when
        val user = userMapper.documentToUser(document)

        // then
        assertThat(user.id).isEqualTo("123e4567-e89b-12d3-a456-426614174000")
        assertThat(user.username).isEqualTo("testuser")
        assertThat(user.password).isEqualTo("securepassword")
        assertThat(user.userType).isEqualTo(UserType.ADMIN)
    }
}
