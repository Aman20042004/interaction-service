package com.streambridge.interaction.service



import com.streambridge.interaction.dto.comment.CreateCommentRequest
import com.streambridge.interaction.dto.comment.DeleteCommentRequest
import com.streambridge.interaction.model.Comment
import com.streambridge.interaction.repository.CommentRepository
import com.streambridge.interaction.exception.ResourceNotFoundException
import com.streambridge.interaction.exception.UnauthorizedActionException
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class CommentService(
    private val commentRepository: CommentRepository
) {

    fun createComment(request: CreateCommentRequest): Comment {

        val now = Instant.now()

        val comment = Comment(
            sessionId = request.sessionId,
            userId = request.userId,
            message = request.message,
            createdAt = now,
            updatedAt = now
        )

        return commentRepository.save(comment)
    }

    fun getCommentsBySession(sessionId: String): List<Comment> {
        return commentRepository.findBySessionIdOrderByCreatedAtAsc(sessionId)
    }

    fun getComment(commentId: String): Comment {
        return commentRepository.findById(commentId)
            .orElseThrow {
                ResourceNotFoundException("Comment not found with id: $commentId")
            }
    }

    fun deleteComment(
        commentId: String,
        request: DeleteCommentRequest
    ) {
        val comment = commentRepository.findById(commentId)
            .orElseThrow {
                ResourceNotFoundException("Comment not found with id: $commentId")
            }

        if (comment.userId != request.userId) {
            throw UnauthorizedActionException(
                "You can delete only your own comment"
            )
        }

        commentRepository.delete(comment)
    }
}