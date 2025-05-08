package data.authentication.datasource.mongo_datasource

import data.authentication.mapper.UserMapper
import kotlinx.coroutines.flow.toList
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.authentication.model.UserDto
import kotlinx.coroutines.flow.firstOrNull

class MongoAuthenticationDataSource(
    private val userCollection: MongoCollection<UserDto>,
) : RemoteAuthenticationDataSource {

    override suspend fun getUserByUserName(userName: String): UserDto? {
        return userCollection.find(Filters.eq(UserMapper.USERNAME_FIELD, userName)).firstOrNull()
    }

    override suspend fun getAllUsers(): List<UserDto> {
        return userCollection.find().toList()
    }

    override suspend fun createUser(user: UserDto) {
        userCollection.insertOne(user)
    }
}
