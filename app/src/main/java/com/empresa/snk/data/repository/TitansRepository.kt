package com.empresa.snk.data.repository

import com.empresa.snk.data.network.SNKApi
import com.empresa.snk.domain.titansDomain.TitansResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TitansRepository @Inject constructor(
    private val snkApi: SNKApi
) {
    suspend fun getTitans(page: String?): TitansResponse {
        return withContext(IO) {
            if (page == null) {
                snkApi.getTitans()

            } else {
                snkApi.getTitansByUrl(page)
            }

        }
    }

}