package data.authentication.datasource.mongo

import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.authentication.model.UserDto
import data.authentication.repository.RemoteAuthenticationDataSource
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.toList

class MongoAuthentication(
    private val userCollection: MongoCollection<UserDto>,
) : RemoteAuthenticationDataSource {

    override suspend fun getUserByUsername(username: String): UserDto? {
        return userCollection.find(Filters.eq(UserDto::username.name, username)).firstOrNull()
    }

    override suspend fun getAllUsers(): List<UserDto> {
        return userCollection.find().toList()
    }

    override suspend fun createUser(userDto: UserDto) {
        userCollection.insertOne(userDto)
    }

}
