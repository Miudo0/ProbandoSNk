package com.empresa.snk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.empresa.snk.ui.PersonajesScreen
import com.empresa.snk.ui.TitansScreen
import com.empresa.snk.ui.theme.SNKTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SNKTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    PersonajesScreen(
//                        innerPadding,
//                    )
                    TitansScreen(
                        innerPadding
                    )

                }
            }
        }
    }
}




