package com.empresa.snk.domain

import com.empresa.snk.data.repository.TitansRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetCurrentInheritorUseCase @Inject constructor(
    private val repository: TitansRepository
) {
    suspend operator fun invoke(currentInheritorUrl: String): String {
        return withContext(IO) {
            val response = repository.getcurrentInheritor(currentInheritorUrl)
            response.name ?: "no disponible"
        }

    }
}