package com.lexaloris.rickandmorty.di

import com.lexaloris.rickandmorty.presentation.view.CharactersFragment
import dagger.Component

@Component(modules = [CharactersModule::class, NetworkModule::class])
interface CharactersComponent {

    companion object {

        fun init(): CharactersComponent {
            return DaggerCharactersComponent.builder().build()
        }
    }

    fun inject(fragment: CharactersFragment)
}
