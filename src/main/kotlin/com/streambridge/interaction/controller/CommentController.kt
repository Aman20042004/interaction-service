package com.streambridge.interaction.controller



import com.streambridge.interaction.dto.comment.CreateCommentRequest
import com.streambridge.interaction.dto.comment.DeleteCommentRequest
import com.streambridge.interaction.model.Comment
import com.streambridge.interaction.service.CommentService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/interactions/comments")
class CommentController(
    private val commentService: CommentService
) {

    @PostMapping
    fun createComment(
        @Valid @RequestBody request: CreateCommentRequest
    ): ResponseEntity<Comment> {

        val comment = commentService.createComment(request)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(comment)
    }

    @GetMapping("/session/{sessionId}")
    fun getCommentsBySession(
        @PathVariable sessionId: String
    ): ResponseEntity<List<Comment>> {

        return ResponseEntity.ok(
            commentService.getCommentsBySession(sessionId)
        )
    }

    @GetMapping("/{commentId}")
    fun getComment(
        @PathVariable commentId: String
    ): ResponseEntity<Comment> {

        return ResponseEntity.ok(
            commentService.getComment(commentId)
        )
    }

    @DeleteMapping("/{commentId}")
    fun deleteComment(
        @PathVariable commentId: String,
        @Valid @RequestBody request: DeleteCommentRequest
    ): ResponseEntity<Void> {

        commentService.deleteComment(
            commentId = commentId,
            request = request
        )

        return ResponseEntity.noContent().build()
    }
}