package com.empresa.snk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.empresa.snk.domain.Info
import com.empresa.snk.ui.CharacterState
import com.empresa.snk.ui.GetCharactersViewModel
import com.empresa.snk.ui.theme.SNKTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.annotation.meta.When


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SNKTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        innerPadding,

                        )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    innerPadding: PaddingValues,
    viewModel: GetCharactersViewModel = hiltViewModel()

) {
    val state by viewModel.characters.collectAsState(CharacterState.Loading)


    LaunchedEffect(Unit) {
        viewModel.getCharacters()
    }

    when (val current = state) {

        is CharacterState.Success -> {
            val personajes = current.characters
            if (personajes.isNotEmpty()) {
                LazyColumn(contentPadding = innerPadding) {
                    items(personajes) {
                        Text(text = it.name.toString())
                    }

                }
            }
        }
        is CharacterState.Error -> {Text(text = current.message)}
        is CharacterState.Loading -> {Text(text = "Cargando")}


    }


}

