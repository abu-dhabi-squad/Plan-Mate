package data.authentication.mapper

import data.authentication.model.UserDto
import logic.model.User
import logic.model.UserType

class UserMapper {

    fun userToDto(user: User): UserDto {
        return UserDto(
            id = user.id,
            username = user.username,
            password = user.password,
            userType = user.userType.name
        )
    }

    fun dtoToUser(dto: UserDto): User {
        return User(
            id = dto.id,
            username = dto.username,
            password = dto.password,
            userType = UserType.valueOf(dto.userType)
        )
    }
}