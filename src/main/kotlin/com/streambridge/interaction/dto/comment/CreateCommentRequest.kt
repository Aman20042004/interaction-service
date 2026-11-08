package com.streambridge.interaction.dto.comment



import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateCommentRequest(

    @field:NotBlank(message = "sessionId is required")
    val sessionId: String,

    @field:NotBlank(message = "userId is required")
    val userId: String,

    @field:NotBlank(message = "message is required")
    @field:Size(
        max = 1000,
        message = "message must not exceed 1000 characters"
    )
    val message: String
)