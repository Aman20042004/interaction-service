package com.streambridge.interaction.dto.comment



import jakarta.validation.constraints.NotBlank

data class DeleteCommentRequest(

    @field:NotBlank(message = "userId is required")
    val userId: String
)