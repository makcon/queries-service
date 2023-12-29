package makcon.queries.domain.exception

import kotlin.reflect.KClass

class ModelNotFoundException(modelClass: KClass<*>, id: Any) :
    RuntimeException("'${modelClass.simpleName}' not found by id: $id")