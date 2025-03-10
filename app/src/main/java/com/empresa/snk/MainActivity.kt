package com.empresa.snk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.empresa.snk.ui.PersonajesScreen
import com.empresa.snk.ui.SnkTopAppBar
import com.empresa.snk.ui.theme.SNKTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val searchText = remember { mutableStateOf("") }
            SNKTheme {
                Scaffold(
                    topBar = {
                        SnkTopAppBar(searchText = searchText)
                    }
                ) { paddingValues ->
//                    EpisodesScreen(paddingValues = paddingValues)
                    PersonajesScreen(
                        paddingValues = paddingValues
                    )
//                    TitansScreen(
//                        innerPadding
//                    )

                }
            }
        }
    }
}




