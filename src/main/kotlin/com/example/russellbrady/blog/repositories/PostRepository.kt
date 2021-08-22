package com.example.russellbrady.blog.repositories

import com.example.russellbrady.blog.models.Post
import java.util.Optional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface PostRepository : JpaRepository<Post, Int> {

    @Query(
        """
            select p from Post p
            join fetch p.author
            where p.id = :id
        """
    )
    override fun findById(id: Int): Optional<Post>
}
