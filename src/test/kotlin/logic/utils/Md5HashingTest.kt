package logic.hashing

import com.google.common.truth.Truth.assertThat
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import squad.abudhabi.logic.utils.HashingService

class HashingServiceTest {

    private lateinit var hashingService: HashingService

    @BeforeEach
    fun setup() {
        hashingService = mockk(relaxed = true)
    }

    @Test
    fun `should return correct hash when input known password`() {
        // given
        val password = "Omar"
        val expectedHash = "9122a13c65aed7b7b3a95c0fd7d4c0b1"
        every { hashingService.hash(password) } returns expectedHash

        // when
        val actualHash = hashingService.hash(password)

        // then
        assertThat(actualHash).isEqualTo(expectedHash)
    }

    @Test
    fun `should always return hash of length 32 when input any password with any length`() {
        // given
        val input1 = "ALaa"
        val input2 = "AlaaKhaled"
        val hash1 = "b2cfa418b0d7d20cabb4f9424b46076e"
        val hash2 = "e7f6a2d261c580c486a2a0b4e49d9d40"
        every { hashingService.hash(input1) } returns hash1
        every { hashingService.hash(input2) } returns hash2

        // when
        val result1 = hashingService.hash(input1)
        val result2 = hashingService.hash(input2)

        // then
        assertThat(result1.length).isEqualTo(32)
        assertThat(result2.length).isEqualTo(32)
    }

    @Test
    fun `should return same hash when send same password`() {
        // given
        val password = "anyThing"
        val expectedHash = "9f6f2fcffb7a8ac7c34bc67425135b25"
        every { hashingService.hash(password) } returns expectedHash

        // when
        val hash1 = hashingService.hash(password)
        val hash2 = hashingService.hash(password)

        // then
        assertThat(hash1).isEqualTo(hash2)
    }


}
