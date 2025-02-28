package com.empresa.snk.domain

import com.empresa.snk.data.repository.CharactersRepository
import com.empresa.snk.domain.charactersDomain.CharactersResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class getCharactersUseCase @Inject constructor(
    private val repository: CharactersRepository
) {

    suspend operator fun invoke(pageUrl: String?): CharactersResponse {
        return withContext(Dispatchers.IO) {
            repository.getCharacters(pageUrl)
        }
    }


}