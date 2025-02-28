package com.empresa.snk.domain

import com.empresa.snk.data.repository.TitansRepository
import com.empresa.snk.domain.titansDomain.TitansResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetTitansUseCase @Inject constructor(
    private val titansRepository: TitansRepository
) {
    suspend operator fun invoke(page: String?): TitansResponse {
        return withContext(IO){
            titansRepository.getTitans(page)
        }
    }

}