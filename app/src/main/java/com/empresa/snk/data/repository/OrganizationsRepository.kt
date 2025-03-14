package com.empresa.snk.data.repository

import com.empresa.snk.data.network.SNKApiOrganization
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




}