package com.empresa.snk.domain

import com.empresa.snk.data.repository.OrganizationsRepository

import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetNotableMembersUseCase @Inject constructor(
    private val repository: OrganizationsRepository
)  {
    suspend operator fun invoke(notableMembersUrl: String): String{
      return withContext(IO){
          val response = repository.getNotableMembers(notableMembersUrl)
          response.name ?: "no disponible"
      }
    }

}