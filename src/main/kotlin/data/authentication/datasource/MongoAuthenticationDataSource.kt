package data.authentication.datasource

import com.mongodb.client.MongoCollection
import data.authentication.UserMapper
import org.bson.Document
import squad.abudhabi.logic.model.User

class MongoAuthenticationDataSource(
    private val collection: MongoCollection<Document>,
    private val mapper: UserMapper
): AuthenticationDataSource {
    override suspend fun getUserByUserName(userName: String): User? {
        return collection.find(Document(UserMapper.USERNAME_FIELD, userName)).first()
            ?.let { mapper.documentToUser(it) }
    }

    override suspend fun getAllUsers(): List<User> {
        return collection.find().map { mapper.documentToUser(it) }.toList()
    }

    override suspend fun createUser(user: User) {
       mapper.userToDocument(user).apply { collection.insertOne(this) }
    }

}