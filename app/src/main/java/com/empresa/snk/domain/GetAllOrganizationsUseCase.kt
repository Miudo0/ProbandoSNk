package com.empresa.snk.domain

import com.empresa.snk.data.repository.OrganizationsRepository
import com.empresa.snk.domain.OrganizationsDomain.OrganizationsResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAllOrganizationsUseCase @Inject constructor(
private val repository: OrganizationsRepository
){
    suspend operator fun invoke() : OrganizationsResponse {
        return withContext(Dispatchers.IO) {
            repository.getOrganizations()
        }
    }



}