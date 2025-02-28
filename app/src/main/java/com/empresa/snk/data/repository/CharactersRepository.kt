package com.empresa.snk.data.repository

import com.empresa.snk.data.network.SNKApi
import com.empresa.snk.domain.ApiResponse
import com.empresa.snk.domain.Characters
import com.empresa.snk.domain.Info
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharactersRepository @Inject constructor(
private val SNKApi: SNKApi
){
    suspend fun getCharacters(): List<Characters>?{
        return withContext(Dispatchers.IO) {
          val response =  SNKApi.getCharacters()
          response.results
        }
    }
}