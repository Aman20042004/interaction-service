package com.streambridge.interaction.dto.ask



import jakarta.validation.constraints.NotBlank

data class AskActionRequest(

    @field:NotBlank(message = "ownerId is required")
    val ownerId: String
)