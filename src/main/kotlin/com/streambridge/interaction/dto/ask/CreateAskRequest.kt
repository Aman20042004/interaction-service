package com.streambridge.interaction.dto.ask



import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateAskRequest(

    @field:NotBlank(message = "sessionId is required")
    val sessionId: String,

    @field:NotBlank(message = "userId is required")
    val userId: String,

    @field:Size(
        max = 1000,
        message = "message must not exceed 1000 characters"
    )
    val message: String? = null
)