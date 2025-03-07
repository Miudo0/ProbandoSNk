package com.empresa.snk.domain

import com.empresa.snk.data.repository.CharactersRepository
import com.empresa.snk.domain.charactersDomain.Characters
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCharactersFilterUseCase @Inject constructor(
    private val characterRepository: CharactersRepository
) {
    suspend operator fun invoke(name: String?, gender: String?,status: String?, occupation: String?): List<Characters> {
        return withContext(IO) {
            characterRepository.getCharactersFilter(name, gender, status, occupation)
        }
    }
}
