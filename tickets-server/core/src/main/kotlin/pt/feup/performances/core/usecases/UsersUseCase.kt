package pt.feup.performances.core.usecases

import pt.feup.performances.core.User
import pt.feup.performances.core.UserRepositoryService

interface UsersUseCase {
    fun registerUser(user: User): String
}

class UsersUseCaseImpl(
    private val usersRepository: UserRepositoryService
) : UsersUseCase {
    override fun registerUser(user: User): String {
        return usersRepository.save(user)
    }

}
