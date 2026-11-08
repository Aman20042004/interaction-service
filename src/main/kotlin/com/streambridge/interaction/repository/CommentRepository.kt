package com.streambridge.interaction.repository



import com.streambridge.interaction.model.Comment
import org.springframework.data.mongodb.repository.MongoRepository

interface CommentRepository : MongoRepository<Comment, String> {

    fun findBySessionIdOrderByCreatedAtAsc(sessionId: String): List<Comment>
}