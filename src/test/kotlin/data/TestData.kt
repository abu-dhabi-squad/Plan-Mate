package data

import logic.model.User
import logic.model.UserType

object TestData {
    val user1 = User("1", "user1", "pass1", UserType.MATE)
    val user2 = User("2", "user2", "pass2", UserType.ADMIN)
    val userString1 = "1,user1,pass1,MATE"
    val userString2 = "2,user2,pass2,ADMIN"
    val userName1 = "user1"
}