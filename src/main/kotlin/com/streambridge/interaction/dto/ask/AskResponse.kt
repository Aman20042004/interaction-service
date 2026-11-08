package com.streambridge.interaction.dto.ask



import com.streambridge.interaction.model.AskStatus
import java.time.Instant

data class AskResponse(

    val id: String?,

    val sessionId: String,

    val userId: String,

    val message: String?,

    val status: AskStatus,

    val createdAt: Instant?,

    val updatedAt: Instant?
)