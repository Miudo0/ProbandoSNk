package com.empresa.snk.ui.Navegacion

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.empresa.snk.ui.PersonajesScreen
import com.empresa.snk.ui.PortadaScreen

@Composable
fun NavigarionWrapper(){
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

    NavHost(navController = navController, startDestination = Portada.route) {
        composable(Portada.route){
            PortadaScreen()
        }

        composable(Characters.route) {
           PersonajesScreen(
               paddingValues = PaddingValues()
           )
        }

    }
}