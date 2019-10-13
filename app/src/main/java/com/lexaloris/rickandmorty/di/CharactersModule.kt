package com.lexaloris.rickandmorty.di

import com.lexaloris.rickandmorty.data.service.CharactersService
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [CharactersModuleBinds::class])
class CharactersModule {

    @Provides
    fun provideCharactersService(retrofit: Retrofit): CharactersService {
        return retrofit.create(CharactersService::class.java)
    }
}
