package com.empresa.snk.data.repository

import com.empresa.snk.data.network.SNKApi
import com.empresa.snk.domain.charactersDomain.Characters

import com.empresa.snk.domain.charactersDomain.CharactersResponse

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharactersRepository @Inject constructor(
    private val SNKApi: SNKApi
) {
    suspend fun getCharacters(pageUrl: String?): CharactersResponse {
        return withContext(Dispatchers.IO) {
            if (pageUrl == null) {
                SNKApi.getCharacters() // Cargar la primera página
            } else {
                SNKApi.getCharactersByUrl(pageUrl) // Cargar la siguiente página
            }
        }
    }

    //funcion para filtrar
    suspend fun getCharactersFilter(
        name: String?,
        gender: String?,
        status: String?,
        occupation: String?
    ): List<Characters> {
        return withContext(Dispatchers.IO) {
            val response = SNKApi.getFilteredCharacters(name, gender, status, occupation)
            response.results ?: emptyList()
        }
    }

}