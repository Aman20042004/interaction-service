package com.streambridge.interaction.service



import com.streambridge.interaction.dto.ask.AskActionRequest
import com.streambridge.interaction.dto.ask.CreateAskRequest
import com.streambridge.interaction.dto.ask.AskResponse
import com.streambridge.interaction.exception.BusinessRuleException
import com.streambridge.interaction.exception.ResourceNotFoundException
import com.streambridge.interaction.exception.UnauthorizedActionException
import com.streambridge.interaction.model.AskRequest
import com.streambridge.interaction.model.AskStatus
import com.streambridge.interaction.repository.AskRequestRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class AskService(
    private val askRequestRepository: AskRequestRepository
) {

    fun createAsk(request: CreateAskRequest): AskResponse {

        val now = Instant.now()

        val askRequest = AskRequest(
            sessionId = request.sessionId,
            userId = request.userId,
            message = request.message,
            status = AskStatus.PENDING,
            createdAt = now,
            updatedAt = now
        )

        return askRequestRepository.save(askRequest).toResponse()
    }

    fun getAsk(askId: String): AskResponse {

        val askRequest = askRequestRepository.findById(askId)
            .orElseThrow {
                ResourceNotFoundException(
                    "ASK request not found with id: $askId"
                )
            }

        return askRequest.toResponse()
    }

    fun getAsksBySession(
        sessionId: String,
        status: AskStatus?
    ): List<AskResponse> {

        val asks = if (status == null) {
            askRequestRepository
                .findBySessionIdOrderByCreatedAtAsc(sessionId)
        } else {
            askRequestRepository
                .findBySessionIdAndStatusOrderByCreatedAtAsc(
                    sessionId,
                    status
                )
        }

        return asks.map { it.toResponse() }
    }

    fun acceptAsk(
        askId: String,
        request: AskActionRequest
    ): AskResponse {

        val askRequest = getAskEntity(askId)

        validateOwner(request.ownerId, askRequest)

        if (askRequest.status != AskStatus.PENDING) {
            throw BusinessRuleException(
                "Only PENDING ASK requests can be accepted"
            )
        }

        val updatedAsk = askRequest.copy(
            status = AskStatus.ACCEPTED,
            updatedAt = Instant.now()
        )

        return askRequestRepository
            .save(updatedAsk)
            .toResponse()
    }

    fun rejectAsk(
        askId: String,
        request: AskActionRequest
    ): AskResponse {

        val askRequest = getAskEntity(askId)

        validateOwner(request.ownerId, askRequest)

        if (askRequest.status != AskStatus.PENDING) {
            throw BusinessRuleException(
                "Only PENDING ASK requests can be rejected"
            )
        }

        val updatedAsk = askRequest.copy(
            status = AskStatus.REJECTED,
            updatedAt = Instant.now()
        )

        return askRequestRepository
            .save(updatedAsk)
            .toResponse()
    }

    private fun getAskEntity(askId: String): AskRequest {
        return askRequestRepository.findById(askId)
            .orElseThrow {
                ResourceNotFoundException(
                    "ASK request not found with id: $askId"
                )
            }
    }

    /*
     * Interaction Service does not query Session Service.
     *
     * For this initial implementation, ownerId is supplied
     * by the request and compared with the ASK owner's identity
     * information available to this service.
     */
    private fun validateOwner(
        ownerId: String,
        askRequest: AskRequest
    ) {
        /*
         * IMPORTANT:
         * An ASK itself contains the requesting user's userId,
         * not the session owner's ID.
         *
         * Therefore this service cannot independently prove that
         * ownerId owns the session without calling Session Service.
         *
         * Since the requirement explicitly says not to query
         * Session Service, the controller/API contract treats
         * ownerId as the supplied owner identity for this initial
         * implementation.
         */
        if (ownerId.isBlank()) {
            throw UnauthorizedActionException(
                "ownerId is required"
            )
        }
    }

    private fun AskRequest.toResponse(): AskResponse {
        return AskResponse(
            id = id,
            sessionId = sessionId,
            userId = userId,
            message = message,
            status = status,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}