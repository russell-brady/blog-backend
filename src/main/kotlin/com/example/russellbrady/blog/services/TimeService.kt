package com.example.russellbrady.blog.services

import java.time.LocalDateTime
import org.springframework.stereotype.Service

interface TimeService {
    fun nowDateTime(): LocalDateTime
}

@Service
class TimeServiceImpl : TimeService {
    override fun nowDateTime(): LocalDateTime {
        return LocalDateTime.now()
    }
}
