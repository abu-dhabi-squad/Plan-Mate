package data.authentication.datasource.mongo_datasource

import kotlinx.coroutines.flow.toList
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.authentication.model.UserDto
import data.authentication.repository.RemoteAuthenticationDataSource
import kotlinx.coroutines.flow.firstOrNull

class MongoAuthenticationDataSource(
    private val userCollection: MongoCollection<UserDto>,
) : RemoteAuthenticationDataSource {

    override suspend fun getUserByUserName(userName: String): UserDto? {
        return userCollection.find(Filters.eq(USERNAME_FIELD, userName)).firstOrNull()
    }

    override suspend fun getAllUsers(): List<UserDto> {
        return userCollection.find().toList()
    }

    override suspend fun createUser(user: UserDto) {
        userCollection.insertOne(user)
    }
    companion object {
        const val USERNAME_FIELD = "username"
    }
}
