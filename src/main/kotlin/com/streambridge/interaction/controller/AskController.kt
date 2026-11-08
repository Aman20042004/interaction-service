package com.streambridge.interaction.controller




import com.streambridge.interaction.dto.ask.AskActionRequest
import com.streambridge.interaction.dto.ask.AskResponse
import com.streambridge.interaction.dto.ask.CreateAskRequest
import com.streambridge.interaction.model.AskStatus
import com.streambridge.interaction.service.AskService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/interactions/ask")
class AskController(
    private val askService: AskService
) {

    @PostMapping
    fun createAsk(
        @Valid @RequestBody request: CreateAskRequest
    ): ResponseEntity<AskResponse> {

        val ask = askService.createAsk(request)

        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ask)
    }

    @GetMapping("/{askId}")
    fun getAsk(
        @PathVariable askId: String
    ): ResponseEntity<AskResponse> {

        return ResponseEntity.ok(
            askService.getAsk(askId)
        )
    }

    @GetMapping("/session/{sessionId}")
    fun getAsksBySession(
        @PathVariable sessionId: String,
        @RequestParam(required = false) status: AskStatus?
    ): ResponseEntity<List<AskResponse>> {

        return ResponseEntity.ok(
            askService.getAsksBySession(
                sessionId = sessionId,
                status = status
            )
        )
    }

    @PostMapping("/{askId}/accept")
    fun acceptAsk(
        @PathVariable askId: String,
        @Valid @RequestBody request: AskActionRequest
    ): ResponseEntity<AskResponse> {

        return ResponseEntity.ok(
            askService.acceptAsk(
                askId = askId,
                request = request
            )
        )
    }

    @PostMapping("/{askId}/reject")
    fun rejectAsk(
        @PathVariable askId: String,
        @Valid @RequestBody request: AskActionRequest
    ): ResponseEntity<AskResponse> {

        return ResponseEntity.ok(
            askService.rejectAsk(
                askId = askId,
                request = request
            )
        )
    }
}