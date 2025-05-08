package data.authentication.datasource.mongo_datasource

import data.authentication.mapper.UserMapper
import kotlinx.coroutines.flow.toList
import com.mongodb.client.model.Filters
import com.mongodb.kotlin.client.coroutine.MongoCollection
import data.authentication.model.UserDto
import kotlinx.coroutines.flow.firstOrNull

class MongoAuthenticationDataSource(
    private val collection: MongoCollection<UserDto>,
) : RemoteAuthenticationDataSource {

    override suspend fun getUserByUserName(userName: String): UserDto? {
        return collection.find(Filters.eq(UserMapper.USERNAME_FIELD, userName)).firstOrNull()
    }

    override suspend fun getAllUsers(): List<UserDto> {
        return collection.find().toList()
    }

    override suspend fun createUser(user: UserDto) {
        collection.insertOne(user)
    }
}
