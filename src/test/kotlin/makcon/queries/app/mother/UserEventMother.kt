package makcon.queries.app.mother

import makcon.queries.app.utils.Rand
import makcon.queries.external.user.UserCreatedEventV1
import makcon.queries.external.user.UserDeletedEventV1
import makcon.queries.external.user.UserEventDataV1
import makcon.queries.external.user.UserUpdatedEventV1

object UserCreatedEventMother {
    
    fun of(
        userId: String = Rand.uuidStr(),
        data: UserEventDataV1 = UserEventDataV1Mother.of(),
    ) = UserCreatedEventV1(
        userId = userId,
        data = data,
    )
}

object UserUpdatedEventMother {
    
    fun of(
        userId: String = Rand.uuidStr(),
        dataPrev: UserEventDataV1 = UserEventDataV1Mother.of(),
        dataNext: UserEventDataV1 = UserEventDataV1Mother.of(),
    ) = UserUpdatedEventV1(
        userId = userId,
        dataPrev = dataPrev,
        dataNext = dataNext,
    )
}

object UserDeletedEventMother {
    
    fun of(
        userId: String = Rand.uuidStr(),
    ) = UserDeletedEventV1(
        userId = userId,
    )
}

object UserEventDataV1Mother {
    
    fun of(
        firstName: String = Rand.string(),
        lastName: String = Rand.string(),
        email: String = Rand.string(),
    ) = UserEventDataV1(
        firstName = firstName,
        lastName = lastName,
        email = email,
    )
}