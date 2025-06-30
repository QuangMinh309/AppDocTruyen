package com.example.frontend.data.repository
import android.util.Log
import com.example.frontend.data.model.Category
import com.example.frontend.data.model.onFailure
import com.example.frontend.data.model.onSuccess
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CategoryProvider @Inject constructor(
    private val repository: CategoryRepository
) {

    suspend fun fetchAllCategory(): List<Category> {
        val result = repository.getCategories()
        var catList = emptyList<Category>()

        result.onSuccess { list ->
            catList=list
        }.onFailure { error ->
            Log.e("apiError","Error: ${error.message}")
            catList= emptyList()
        }
        return catList
    }
}
