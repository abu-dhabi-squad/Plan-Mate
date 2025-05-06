package data.authentication.datasource.mongo_datasource

import com.mongodb.client.MongoCollection
import data.authentication.datasource.AuthenticationDataSource
import data.authentication.usermapper.UserMapper
import org.bson.Document
import logic.model.User

class MongoAuthenticationDataSource(
    private val collection: MongoCollection<Document>,
    private val mapper: UserMapper
): AuthenticationDataSource {
    override suspend fun getUserByUserName(userName: String): User? {
        return collection.find(Document(UserMapper.Companion.USERNAME_FIELD, userName)).first()
            ?.let { mapper.documentToUser(it) }
    }

    override suspend fun getAllUsers(): List<User> {
        return collection.find().map { mapper.documentToUser(it) }.toList()
    }

    override suspend fun createUser(user: User) {
       mapper.userToDocument(user).apply { collection.insertOne(this) }
    }

}