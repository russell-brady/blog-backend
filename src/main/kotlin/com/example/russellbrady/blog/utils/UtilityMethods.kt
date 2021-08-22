package com.example.russellbrady.blog.utils

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

val jacksonObjectMapper = jacksonObjectMapper().apply {
    configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
    registerModule(JavaTimeModule())
}

inline fun <reified T> String.toObject(): T {
    return jacksonObjectMapper.readValue(this, T::class.java)
}

inline fun <reified T> String.toObjectList(): List<T> {
    return jacksonObjectMapper.readValue(
        this,
        jacksonObjectMapper.typeFactory.constructCollectionType(List::class.java, T::class.java)
    )
}

fun Any.toJson(): String {
    return jacksonObjectMapper.writeValueAsString(this)
}
