package com.techmobile.artbooktesting.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.techmobile.artbooktesting.model.ImageResponse
import com.techmobile.artbooktesting.roomdb.Art
import com.techmobile.artbooktesting.utils.Resource

class FakeArtRepository : ArtRepositoryInterface {

    private val arts = mutableListOf<Art>()
    private val artsLiveData = MutableLiveData<List<Art>>(arts)
    override suspend fun insertArt(art: Art) {
        arts.add(art)
        refreshData()
    }

    override suspend fun deleteArt(art: Art) {
       arts.remove(art)
        refreshData()
    }

    override fun getArt(): LiveData<List<Art>> {
        return artsLiveData
    }

    override suspend fun searchImage(imageString: String): Resource<ImageResponse> {
       return Resource.success(ImageResponse(0,0, listOf()))
    }
    private fun refreshData(){
        artsLiveData.postValue(arts)
    }
}