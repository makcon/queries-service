package makcon.queries.adapters.exception

import makcon.queries.domain.model.constant.FilterField

class FieldMappingException(val field: FilterField, message: String) :
    RuntimeException("Failed to map field: $message")