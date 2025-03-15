package com.empresa.snk.domain.OrganizationsDomain

import com.google.gson.annotations.SerializedName

data class OrganizationsResponse
    (
    @SerializedName("info") var info: InfoOrganizations? = InfoOrganizations(),
    @SerializedName("results") var results: ArrayList<Organization> = arrayListOf()

)