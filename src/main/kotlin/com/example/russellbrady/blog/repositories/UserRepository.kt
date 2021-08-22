package com.example.russellbrady.blog.repositories

import com.example.russellbrady.blog.models.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Int> {

    fun findByEmailAddress(emailAddress: String): User?

    @Query(
        """
            select u from User u
            left join fetch u.posts
            where u.username = :username
        """
    )
    fun findByUsername(username: String): User?
}
