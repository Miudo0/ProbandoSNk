package com.empresa.snk.data.network

import com.empresa.snk.domain.charactersDomain.Personaje
import com.empresa.snk.domain.organizationsDomain.OrganizationsResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface SNKApiOrganization {

    @GET("organizations")
    suspend fun getOrganizations(): OrganizationsResponse

    @GET
    suspend fun getNotableMembers(@Url notableMembersUrl: String): Personaje

    @GET
    suspend fun getNotableFormerMembers(@Url notableFormerMembersUrl: String): Personaje

}