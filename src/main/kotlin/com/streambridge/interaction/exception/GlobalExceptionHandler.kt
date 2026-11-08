package com.streambridge.interaction.exception



import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

@RestControllerAdvice
class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidationException(
        exception: MethodArgumentNotValidException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {

        val message = exception.bindingResult
            .fieldErrors
            .joinToString(", ") { error ->
                "${error.field}: ${error.defaultMessage}"
            }

        return buildResponse(
            status = HttpStatus.BAD_REQUEST,
            error = "VALIDATION_ERROR",
            message = message,
            path = request.requestURI
        )
    }

    @ExceptionHandler(ResourceNotFoundException::class)
    fun handleNotFoundException(
        exception: ResourceNotFoundException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {

        return buildResponse(
            status = HttpStatus.NOT_FOUND,
            error = "NOT_FOUND",
            message = exception.message ?: "Resource not found",
            path = request.requestURI
        )
    }

    @ExceptionHandler(UnauthorizedActionException::class)
    fun handleUnauthorizedException(
        exception: UnauthorizedActionException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {

        return buildResponse(
            status = HttpStatus.FORBIDDEN,
            error = "FORBIDDEN",
            message = exception.message ?: "Action not allowed",
            path = request.requestURI
        )
    }

    @ExceptionHandler(BusinessRuleException::class)
    fun handleBusinessRuleException(
        exception: BusinessRuleException,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {

        return buildResponse(
            status = HttpStatus.BAD_REQUEST,
            error = "BUSINESS_RULE_ERROR",
            message = exception.message ?: "Business rule violation",
            path = request.requestURI
        )
    }

    @ExceptionHandler(Exception::class)
    fun handleUnexpectedException(
        exception: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {

        return buildResponse(
            status = HttpStatus.INTERNAL_SERVER_ERROR,
            error = "INTERNAL_SERVER_ERROR",
            message = "An unexpected error occurred",
            path = request.requestURI
        )
    }

    private fun buildResponse(
        status: HttpStatus,
        error: String,
        message: String,
        path: String
    ): ResponseEntity<ErrorResponse> {

        val response = ErrorResponse(
            timestamp = Instant.now(),
            status = status.value(),
            error = error,
            message = message,
            path = path
        )

        return ResponseEntity.status(status).body(response)
    }
}

data class ErrorResponse(
    val timestamp: Instant,
    val status: Int,
    val error: String,
    val message: String,
    val path: String
)