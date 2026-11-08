package com.streambridge.interaction.model



import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.Instant

@Document(collection = "ask_requests")
data class AskRequest(

    @Id
    val id: String? = null,

    val sessionId: String,

    val userId: String,

    val message: String? = null,

    val status: AskStatus = AskStatus.PENDING,

    val createdAt: Instant? = null,

    val updatedAt: Instant? = null
)