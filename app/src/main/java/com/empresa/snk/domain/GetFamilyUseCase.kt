package com.empresa.snk.domain

import com.empresa.snk.data.repository.CharactersRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetFamilyUseCase @Inject constructor(
    private val repository: CharactersRepository
) {
    suspend operator fun invoke(familyUrl: String) : String{
        return withContext(IO){
           try {
            val response =   repository.getFamilyMembers(familyUrl)
           response?.name ?: "No disponible"
           }catch (e: Exception){
             "error al cargar"
           }
        }
    }

}