package com.empresa.snk.data.repository

import com.empresa.snk.data.network.SNKApiOrganization
import com.empresa.snk.domain.charactersDomain.Personaje
import com.empresa.snk.domain.organizationsDomain.OrganizationsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class OrganizationsRepository @Inject constructor(
    private val SNKApiOrganizations: SNKApiOrganization
) {

    suspend fun getOrganizations(): OrganizationsResponse {
        return withContext(Dispatchers.IO)  {
            SNKApiOrganizations.getOrganizations()
        }
    }

    suspend fun getNotableMembers(notableMembersUrl: String): Personaje {
        return withContext(Dispatchers.IO){
            try {
                SNKApiOrganizations.getNotableMembers(notableMembersUrl)
            } catch (e: Exception) {
                null ?: Personaje()
            }
        }
    }

    suspend fun getNotableFormerMembers(notableFormerMembersUrl: String): Personaje {
        return withContext(Dispatchers.IO){
            try {
                SNKApiOrganizations.getNotableMembers(notableFormerMembersUrl)
            } catch (e: Exception) {
                null ?: Personaje()
            }
        }
    }


}