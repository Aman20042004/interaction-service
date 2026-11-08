package com.streambridge.interaction.model



import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "comments")
data class Comment(

    @Id
    val id: String? = null,

    val sessionId: String,

    val userId: String,

    val message: String,

    val createdAt: Instant? = null,

    val updatedAt: Instant? = null
)