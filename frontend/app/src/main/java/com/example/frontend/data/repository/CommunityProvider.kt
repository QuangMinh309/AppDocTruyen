package com.example.frontend.data.repository
import android.util.Log
import com.example.frontend.data.api.CommunityRequest
import com.example.frontend.data.model.Community
import com.example.frontend.data.model.User
import com.example.frontend.data.model.onFailure
import com.example.frontend.data.model.onSuccess
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CommunityProvider @Inject constructor(
    private val repository: CommunityRepository,
    private val imageUrlProvider: ImageUrlProvider
) {

    suspend fun fetchAllCommunity(): List<Community> {
        val result = repository.getAllCommunity()
        var communityList = emptyList<Community>()

        result.onSuccess { list ->
           communityList=list
        }.onFailure { error ->
            Log.e("apiError","Error: ${error.message}")
            communityList= emptyList()
        }
        return communityList
    }
    suspend fun filterCommunity(categoryId: Int,memberNum:Int=0): List<Community> {
        val result = repository.filterCommunity(categoryId,memberNum)
        var communityList = emptyList<Community>()

        result.onSuccess { list ->
           communityList=list
        }.onFailure { error ->
            Log.e("apiError","Error: ${error.message}")
            communityList= emptyList()
        }
        return communityList
    }

    suspend fun getCommunityById(id: Int): Community? {
        val result = repository.getCommunityById(id)
        var community:Community? = null

        result.onSuccess { com ->
            community=com
        }.onFailure { error ->
            Log.e("apiError","Error: ${error.message}")
        }
        return community
    }
    suspend fun searchMembers(id: Int,name:String=""): List<User> {
        val result = repository.searchMembers(id,name)
        var memList:List<User> = emptyList()

        result.onSuccess { list ->
            memList=list
        }.onFailure { error ->
            Log.e("apiError","Error: ${error.message}")
        }
        return memList
    }

    suspend fun createCommunity(community: CommunityRequest, file: File?): Community? {
            val imageInfo = imageUrlProvider.uploadImage(file)
            val result = repository.createCommunity(community, imageInfo)
            var createdCommunity:Community? = null

            result.onSuccess { com ->
                createdCommunity = com
            }.onFailure { error ->
                Log.e("apiError","Error: ${error.message}")
            }
            return createdCommunity

    }

    suspend fun updateCommunity( community: Community, file:File?): Community? {
        val imageInfo = imageUrlProvider.uploadImage(file)
        val result = repository.updateCommunity(community, imageInfo)
        var updateCommunity:Community? = null

        result.onSuccess { com ->
            updateCommunity = com
        }.onFailure { error ->
            Log.e("apiError","Error: ${error.message}")
        }
        return updateCommunity
    }

    suspend fun deleteCommunity(id: Int) {
        val result = repository.deleteCommunity(id)
        result.onSuccess {
            //do nothing
        }.onFailure { error ->
            Log.e("apiError","Error: ${error.message}")
        }
    }
}
