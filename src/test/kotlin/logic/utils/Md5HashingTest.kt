package logic.utils

import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class Md5HashingTest {

    private lateinit var md5Hashing: Md5Hashing

    @BeforeEach
    fun setup() {
        md5Hashing = Md5Hashing()
    }

    @Test
    fun `should return correct hash when input known password`() {
        // Given
        val password = "Omar"
        val expectedHash = "a3b0d8be3bc69a1c348a6f2ec974a3eb"

        // When
        val actualHash = md5Hashing.hash(password)

        // Then
        assertThat(actualHash).isEqualTo(expectedHash)
    }

    @Test
    fun `should always return hash of length 32 when input any password with any length`() {
        // Given
        val input1 = "ALaa"
        val input2 = "AlaaKhaled"

        // When
        val result1 = md5Hashing.hash(input1)
        val result2 = md5Hashing.hash(input2)

        // Then
        assertThat(result1.length).isEqualTo(32)
        assertThat(result2.length).isEqualTo(32)
    }

    @Test
    fun `should return same hash when send same password`() {
        // Given
        val password = "anyThing"

        // When
        val hash1 = md5Hashing.hash(password)
        val hash2 = md5Hashing.hash(password)

        // Then
        assertThat(hash1).isEqualTo(hash2)
    }

}