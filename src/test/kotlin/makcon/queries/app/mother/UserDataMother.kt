package makcon.queries.app.mother

import makcon.queries.app.utils.Rand
import makcon.queries.domain.model.UserData

object UserDataMother {
    
    fun of(
        firstName: String = Rand.string(),
        lastName: String = Rand.string(),
        email: String = Rand.string(),
    ) = UserData(
        firstName = firstName,
        lastName = lastName,
        email = email
    )
}