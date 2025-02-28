package com.empresa.snk.data.repository

import com.empresa.snk.data.network.SNKApi

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
}