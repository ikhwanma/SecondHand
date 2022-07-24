package binar.lima.satu.secondhand.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import binar.lima.satu.secondhand.data.remote.ApiService
import binar.lima.satu.secondhand.model.product.GetProductResponseItem
import kotlinx.coroutines.delay
import java.lang.Exception

class PagingSource(private val apiService: ApiService, private val idCategory: Int) : PagingSource<Int, GetProductResponseItem>()  {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GetProductResponseItem> {
        return try {
            val currentPage = params.key ?: 1
            val response = if (idCategory != 0){
                apiService.getAllProductPaging(status = "available", category_id = idCategory, search = null, page = currentPage, perPage = 13)
            }else{
                apiService.getAllProductPaging(status = "available", category_id = null, search = null, page = currentPage, perPage = 13)
            }

            val data = response.body() ?: emptyList()
            val responseData = mutableListOf<GetProductResponseItem>()
            responseData.addAll(data)

            delay(2500)
            if (idCategory != 0){
                LoadResult.Page(data = responseData, prevKey = null,
                    nextKey =  if (data.isEmpty())null else currentPage.plus(1))
            }else{
                LoadResult.Page(data = responseData, prevKey = null,
                    nextKey = if (currentPage != 11) currentPage.plus(1) else null )
            }

        }catch (e: Exception){
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GetProductResponseItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}