package com.empresa.snk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import com.empresa.snk.ui.PersonajesScreen
import com.empresa.snk.ui.theme.SNKTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SNKTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = { Text("SNK") },
                            colors = topAppBarColors(
                                containerColor = colors.primary,
                                titleContentColor = colors.onPrimary,
                            )
                        )
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




