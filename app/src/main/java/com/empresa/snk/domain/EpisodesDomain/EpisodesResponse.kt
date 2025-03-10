package com.empresa.snk.domain.EpisodesDomain

import com.google.gson.annotations.SerializedName

data class EpisodesResponse (
    @SerializedName("info"    ) var info    : InfoEpisodes?           = InfoEpisodes(),
    @SerializedName("results" ) var results : ArrayList<Episodes> = arrayListOf()
)