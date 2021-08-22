package com.example.russellbrady.blog.models

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "users")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int,

    val username: String,

    val emailAddress: String,

    val password: String,

    @OneToMany(mappedBy = "author")
    val posts: Set<Post>
) {
    constructor(username: String, emailAddress: String, password: String) :
        this(-1, username, emailAddress, password, setOf())

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false

        if (id != other.id) return false
        if (username != other.username) return false
        if (emailAddress != other.emailAddress) return false
        if (password != other.password) return false
        if (posts != other.posts) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + username.hashCode()
        result = 31 * result + emailAddress.hashCode()
        result = 31 * result + password.hashCode()
        result = 31 * result + posts.hashCode()
        return result
    }

    override fun toString(): String {
        return "User(id=$id, username='$username', emailAddress='$emailAddress', password='$password', posts=$posts)"
    }
}
