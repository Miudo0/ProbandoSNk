package com.empresa.snk.domain

import com.empresa.snk.data.repository.TitansRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetFormerInheritorsUseCase @Inject constructor(
    private val repository: TitansRepository
) {
    suspend operator fun invoke(formerInheritorUrl: String): String {
        return withContext(IO) {
            val response = repository.getFormerInheritors(formerInheritorUrl)
            response.name ?: "no disponible"
        }
    }
}