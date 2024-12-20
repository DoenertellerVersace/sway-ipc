package model

data class CommandResult(
    val success: Boolean,
    val error: String? = null,
    val parse_error: Boolean? = null
)