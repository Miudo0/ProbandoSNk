package com.empresa.snk.domain

import com.empresa.snk.data.repository.LocationsRepository
import com.empresa.snk.domain.locationsDomain.LocationsResponse
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetAllLocationsUseCase @Inject constructor(
    private val repository: LocationsRepository
) {
  suspend operator fun invoke(pageUrl: String?) : LocationsResponse {
      return withContext(IO){
          repository.getAllLocations(pageUrl)
      }
  }

}