package com.empresa.snk.domain

import com.empresa.snk.data.repository.CharactersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class getCharactersUseCase @Inject constructor(
    private val repository: CharactersRepository
) {
    suspend operator fun invoke():List<Characters>? {
        return withContext(Dispatchers.IO) {
         repository.getCharacters()
        }
    }

}