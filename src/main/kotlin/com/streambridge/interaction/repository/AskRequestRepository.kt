package com.streambridge.interaction.repository



import com.streambridge.interaction.model.AskRequest
import com.streambridge.interaction.model.AskStatus
import org.springframework.data.mongodb.repository.MongoRepository

interface AskRequestRepository : MongoRepository<AskRequest, String> {

    fun findBySessionIdOrderByCreatedAtAsc(sessionId: String): List<AskRequest>

    fun findBySessionIdAndStatusOrderByCreatedAtAsc(
        sessionId: String,
        status: AskStatus
    ): List<AskRequest>
}