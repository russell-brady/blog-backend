package com.example.russellbrady.blog.api

import com.example.russellbrady.blog.dto.PostDto
import com.example.russellbrady.blog.dto.PostForm
import com.example.russellbrady.blog.services.PostService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/users/{username}/posts")
class PostController(
    val postService: PostService
) {

    @GetMapping
    fun getUserPosts(@PathVariable username: String): ResponseEntity<List<PostDto>> {
        return ResponseEntity.ok(postService.getUserPosts(username))
    }

    @GetMapping("/{id}")
    fun getPost(@PathVariable id: Int): ResponseEntity<PostDto> {
        return ResponseEntity.ok(postService.getPost(id))
    }

    @PostMapping
    fun createPost(@RequestBody postForm: PostForm): ResponseEntity<Unit> {
        return ResponseEntity.ok(postService.createPost(postForm))
    }

    @PatchMapping("/{id}")
    fun updatePost(@PathVariable id: Int, @RequestBody postForm: PostForm): ResponseEntity<Unit> {
        return ResponseEntity.ok(postService.updatePost(id, postForm))
    }

    @DeleteMapping("/{id}")
    fun deletePost(@PathVariable id: Int): ResponseEntity<Unit> {
        return ResponseEntity.ok(postService.deletePost(id))
    }
}
