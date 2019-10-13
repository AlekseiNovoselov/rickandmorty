package com.lexaloris.rickandmorty.presentation.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.lexaloris.rickandmorty.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showCharactersScreen()
    }

    private fun showCharactersScreen() {
        var fragment = supportFragmentManager.findFragmentById(R.id.activity_main_container) as CharactersFragment?
        if (fragment == null) {
            fragment = CharactersFragment()
            supportFragmentManager.beginTransaction()
                .add(R.id.activity_main_container, fragment)
                .commit()
        }
    }
}
