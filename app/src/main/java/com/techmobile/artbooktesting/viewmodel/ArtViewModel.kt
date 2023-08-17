package com.techmobile.artbooktesting.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.techmobile.artbooktesting.model.ImageResponse
import com.techmobile.artbooktesting.repo.ArtRepositoryInterface
import com.techmobile.artbooktesting.roomdb.Art
import com.techmobile.artbooktesting.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtViewModel @Inject constructor(
 private val repositoryInterface: ArtRepositoryInterface
) : ViewModel() {

    //Art Fragment
    val artList = repositoryInterface.getArt()


    // Image Api Fragment
    private val images = MutableLiveData<Resource<ImageResponse>>()
    val imageList : LiveData<Resource<ImageResponse>>
       get() = images

    private val selectedImage = MutableLiveData<String>()
    val selectedImageUrl : LiveData<String>
     get() = selectedImage

    // Art Details Fragment

    private var insertArtMsg = MutableLiveData<Resource<Art>>()
    val insertArtMessage : LiveData<Resource<Art>>
    get() = insertArtMsg

    fun resetInsertArtMsg(){
        insertArtMsg = MutableLiveData<Resource<Art>>()
    }

    fun setSelectedImage(url : String){
        selectedImage.postValue(url)
    }

    fun deleteArt( art: Art ) = viewModelScope.launch {
        repositoryInterface.deleteArt(art)
    }

    fun insertArt( art  : Art ) = viewModelScope.launch {
        repositoryInterface.insertArt(art)
    }

    fun makeArt( name : String, artistName : String, year: String ){
        if(name.isEmpty() || artistName.isEmpty() || year.isEmpty()){
            insertArtMsg.postValue( Resource.error("Enter  name, artis and year ",null))
            return
        }

        val yearInt = try{
            year.toInt()
        }catch ( e: Exception){
            insertArtMsg.postValue(Resource.error("Year should be number",null))
            return
        }
        val art = Art(name, artistName, yearInt, selectedImage.value ?: "")
        insertArt(art)
        setSelectedImage("")
        insertArtMsg.postValue(Resource.success(art))
    }

    fun searchForImage( imageSearchString  : String ){
        if(imageSearchString.isEmpty() )
            return
        images.value = Resource.loading(null)
        viewModelScope.launch {
            val response  = repositoryInterface.searchImage(imageSearchString)
            images.value = response
        }
    }
}