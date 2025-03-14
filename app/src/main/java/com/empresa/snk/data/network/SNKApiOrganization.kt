package com.empresa.snk.data.network

import com.empresa.snk.domain.organizationsDomain.OrganizationsResponse
import retrofit2.http.GET

interface SNKApiOrganization {

    @GET("organizations")
    suspend fun getOrganizations(): OrganizationsResponse

}