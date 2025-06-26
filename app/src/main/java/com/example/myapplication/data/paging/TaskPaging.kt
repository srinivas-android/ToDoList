package com.example.myapplication.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.myapplication.data.model.TodoItem
import com.example.myapplication.data.source.remote.SetTaskApi

class TaskPaging(
    private val api: SetTaskApi
) : PagingSource<Int, TodoItem>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, TodoItem> {
        return try {
            val offset = params.key ?: 0
            val limit = params.loadSize
            val response = api.getPagedTodos(offset, limit)

            LoadResult.Page(
                data = response.todos,
                prevKey = if (offset == 0) null else (offset - limit).coerceAtLeast(0),
                nextKey = if (offset + limit >= response.limit) null else offset + limit
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, TodoItem>): Int? {
        return state.anchorPosition?.let { pos ->
            state.closestPageToPosition(pos)?.prevKey
                ?: state.closestPageToPosition(pos)?.nextKey
        }
    }
}