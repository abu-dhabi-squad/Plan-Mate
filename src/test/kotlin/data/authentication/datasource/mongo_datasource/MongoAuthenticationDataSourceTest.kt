package data.authentication.datasource.mongo_datasource

import com.google.common.base.Verify.verify
import com.google.common.truth.Truth.assertThat
import com.mongodb.client.FindIterable
import com.mongodb.client.MongoCollection
import com.mongodb.client.MongoCursor
import com.mongodb.client.MongoIterable
import data.authentication.mapper.UserMapper
import io.mockk.*
import kotlinx.coroutines.test.runTest
import logic.model.User
import logic.model.UserType
import org.bson.Document
import org.junit.jupiter.api.BeforeEach
import kotlin.test.Test

class MongoAuthenticationDataSourceTest{
    private lateinit var mongoAuthenticationDataSource: MongoAuthenticationDataSource
    private lateinit var collection: MongoCollection<Document>
    private lateinit var mapper: UserMapper

    @BeforeEach
    fun setup() {
        collection = mockk(relaxed = true)
        mapper = mockk(relaxed = true)
        mongoAuthenticationDataSource = MongoAuthenticationDataSource(collection, mapper)
    }

    @Test
    fun `should return null when user does not exist`() = runTest {
        // Given
        val userName = "nonexistent"
        coEvery { collection.find(Document(UserMapper.Companion.USERNAME_FIELD, userName)).first() } returns null

        // When
        val result = mongoAuthenticationDataSource.getUserByUserName(userName)

        // Then
        assertThat(result).isNull()
    }

    @Test
    fun `should return user when user exists`() = runTest {
        // Given
        val userName = "existing"
        val document = mockk<Document>()
        val user = User(id = "1", username = userName, password = "pass", userType = UserType.ADMIN)
        coEvery { collection.find(Document(UserMapper.Companion.USERNAME_FIELD, userName)).first() } returns document
        coEvery { mapper.documentToUser(document) } returns user

        // When
        val result = mongoAuthenticationDataSource.getUserByUserName(userName)

        // Then
        assertThat(result).isEqualTo(user)
    }

//    @Test
//    fun `should return all users when collection has users`() = runTest {
//        // Given
//        val user1 = User(id = "1", username = "user1", password = "pass1", userType = UserType.MATE)
//        val user2 = User(id = "2", username = "user2", password = "pass2", userType = UserType.ADMIN)
//
//        val document1 = mockk<Document>()
//        val document2 = mockk<Document>()
//        val findIterable = mockk<FindIterable<Document>>()
//        val mongoCursor = mockk<MongoCursor<Document>>()
//
//        coEvery { collection.find() } returns findIterable
//        coEvery { findIterable.iterator() } returns mongoCursor
//
//        // Mock cursor behavior
//        every { mongoCursor.hasNext() } returnsMany listOf(true, true, false)
//        every { mongoCursor.next() } returnsMany listOf(document1, document2)
//
//        every { mapper.documentToUser(document1) } returns user1
//        every { mapper.documentToUser(document2) } returns user2
//
//        // When
//        val result = mongoAuthenticationDataSource.getAllUsers()
//
//        // Then
//        assertThat(result).containsExactly(user1, user2)
//    }




    @Test
        fun `should create user when user is provided`() = runTest {
            // Given
            val user = User(id = "1", username = "user1", password = "pass1", userType = UserType.MATE)
            val document = mockk<Document>()
            coEvery { mapper.userToDocument(user) } returns document

            // When
            mongoAuthenticationDataSource.createUser(user)

            // Then
            coVerify { collection.insertOne(document) }
        }
}