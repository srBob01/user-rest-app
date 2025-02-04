package ru.arsentiev.app.users

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping(path = ["/api/users"])
class UserController(@Autowired private val userRepository: UserRepository) {

    @GetMapping
    fun getAllUsers(): ResponseEntity<List<User>> {
        val users = userRepository.findAll().toList()
        return ResponseEntity(users, HttpStatus.OK)
    }


    @PostMapping
    fun createUser(@RequestBody user: User): ResponseEntity<User> {
        val savedUser = userRepository.save(user);
        return ResponseEntity(savedUser, HttpStatus.CREATED)
    }

    @GetMapping(path = ["/{id}"])
    fun getUserById(@PathVariable("id") userId: Int): ResponseEntity<User> {
        val user = userRepository.findById(userId).orElse(null)
        return if (user != null) {
            ResponseEntity(user, HttpStatus.OK)
        } else {
            ResponseEntity(HttpStatus.NOT_FOUND)
        }
    }

    @PutMapping(path = ["/{id}"])
    fun updateUserById(@PathVariable("id") userId: Int, @RequestBody user: User): ResponseEntity<User> {
        val user = userRepository.findById(userId).orElse(null)
        if (user == null) {
            return ResponseEntity(user, HttpStatus.NOT_FOUND)
        }
        val updateUser = user.copy(username = user.username, email = user.email)
        userRepository.save(updateUser)
        return ResponseEntity(user, HttpStatus.OK)
    }

    @DeleteMapping(path = ["/{id}"])
    fun deleteUserById(@PathVariable("id") userId: Int): ResponseEntity<User> {
        if (!userRepository.existsById(userId)) {
            return ResponseEntity(HttpStatus.NOT_FOUND)
        }
        userRepository.deleteById(userId)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}