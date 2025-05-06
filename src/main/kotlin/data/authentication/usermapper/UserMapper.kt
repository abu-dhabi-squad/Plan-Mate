package data.authentication.usermapper

import org.bson.Document
import logic.model.User
import logic.model.UserType

class UserMapper {
        fun documentToUser(doc: Document): User {
            return User(
                id = doc.getString(ID_FIELD),
                username = doc.getString(USERNAME_FIELD),
                password = doc.getString(PASSWORD_FIELD),
                userType = UserType.valueOf(doc.getString(USER_TYPE_FIELD))
            )
        }

        fun userToDocument(user: User): Document {
            return Document(ID_FIELD, user.id)
                .append(USERNAME_FIELD, user.username)
                .append(PASSWORD_FIELD, user.password)
                .append(USER_TYPE_FIELD, user.userType.name)
        }

        companion object {
            const val ID_FIELD = "id"
            const val USERNAME_FIELD = "username"
            const val PASSWORD_FIELD = "password"
            const val USER_TYPE_FIELD = "userType"
        }
}