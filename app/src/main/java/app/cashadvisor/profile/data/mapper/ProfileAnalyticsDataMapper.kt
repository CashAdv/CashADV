package app.cashadvisor.profile.data.mapper

import app.cashadvisor.profile.data.dto.response.ProfileAnalyticsDto
import app.cashadvisor.profile.data.dto.response.ProfileAnalyticsResponse
import javax.inject.Inject


class ProfileAnalyticsDataMapper @Inject constructor(){
   fun  toProfileAnalyticsDto(profileAnalyticsResponse: ProfileAnalyticsResponse) = ProfileAnalyticsDto(
       statusCode = profileAnalyticsResponse.statusCode,
       message = profileAnalyticsResponse.message,
       responseCurrency = profileAnalyticsResponse.responseCurrency,
       userAnalyticsDto = profileAnalyticsResponse.userAnalyticsDto
   )
}