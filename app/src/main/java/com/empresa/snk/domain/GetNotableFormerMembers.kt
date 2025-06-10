package com.empresa.snk.domain

import com.empresa.snk.data.repository.OrganizationsRepository
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetNotableFormerMembers @Inject constructor(
    private val repository: OrganizationsRepository
    )
    {
        suspend operator fun invoke(notableFormerMembersUrl: String): String {
            return withContext(IO) {
                val response = repository.getNotableFormerMembers(notableFormerMembersUrl)
                response.name ?: "no disponible"
            }
        }

    }
