package ru.arsentiev.app.users

import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<User, Int>