package data

import logic.model.User
import logic.model.UserType
import java.util.*

object TestData {
    val user1 = User(UUID.fromString("1"), "user1", "pass1", UserType.MATE)
    val user2 = User(UUID.fromString("2"), "user2", "pass2", UserType.ADMIN)
    val userString1 = "1,user1,pass1,MATE"
    val userString2 = "2,user2,pass2,ADMIN"
}