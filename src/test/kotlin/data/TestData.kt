package data

import logic.model.User
import logic.model.UserType
import java.util.*

object TestData {
    val user1 = User(UUID.fromString("550e8400-e29b-41d4-a716-446655440000"), "user1", "pass1", UserType.MATE)
    val user2 = User(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"), "user2", "pass2", UserType.ADMIN)
    val userString1 = "1,user1,pass1,MATE"
    val userString2 = "2,user2,pass2,ADMIN"
}