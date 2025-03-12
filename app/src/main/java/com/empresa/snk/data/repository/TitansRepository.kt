package com.empresa.snk.data.repository

import com.empresa.snk.data.network.SNKApi
import com.empresa.snk.domain.charactersDomain.Personaje
import com.empresa.snk.domain.titansDomain.TitansResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TitansRepository @Inject constructor(
    private val snkApi: SNKApi
) {
    //todos los titanes
    suspend fun getTitans(page: String?): TitansResponse {
        return withContext(IO) {
            if (page == null) {
                snkApi.getTitans()

            } else {
                snkApi.getTitansByUrl(page)
            }

        }
    }

    //heredero actual
    suspend fun getcurrentInheritor(currentInheritorUrl: String): Personaje {
        return withContext(IO) {
            try {
                snkApi.getCurrentInheritor(currentInheritorUrl)
            } catch (e: Exception) {
                null ?: Personaje()
            }
        }
    }

    //herederos anteriores
    suspend fun getFormerInheritors(formerInheritorUrl: String): Personaje {
        return withContext(IO) {
            try {
                snkApi.getFormerInheritors(formerInheritorUrl)
            } catch (e: Exception) {
                null ?: Personaje()
            }

        }
    }
}