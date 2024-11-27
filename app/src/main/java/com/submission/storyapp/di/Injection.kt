package com.submission.storyapp.di

import android.content.Context
import com.submission.storyapp.data.StoryRepository
import com.submission.storyapp.data.pref.UserPreference
import com.submission.storyapp.data.pref.datastore
import com.submission.storyapp.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.datastore)
//        val user = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.getApiService()
        return StoryRepository.getInstance(apiService, pref)
    }
}

