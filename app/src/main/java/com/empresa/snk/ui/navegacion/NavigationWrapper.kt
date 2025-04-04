package com.empresa.snk.ui.navegacion

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.empresa.snk.ui.EpisodesScreen
import com.empresa.snk.ui.LocationsScreen
import com.empresa.snk.ui.OrganizationsScreen
import com.empresa.snk.ui.PersonjesScreen
import com.empresa.snk.ui.PortadaScreen
import com.empresa.snk.ui.TitansScreen

@Composable
fun NavigarionWrapper(){
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination

    NavHost(navController = navController, startDestination = Portada.route) {
        composable(Portada.route){
            PortadaScreen(
                navigateToCharacters = { navController.navigate(Characters.route) },
                navigateToTitans = { navController.navigate(Titans.route) },
                navigateToEpisodes = { navController.navigate(Episodes.route) },
                navigateToOrganizations = {navController.navigate(Organizations.route)},
                navigateToLocations = {navController.navigate(Locations.route)}

            )
        }

        composable(Characters.route) {
         PersonjesScreen(
               paddingValues = PaddingValues()
           )
        }
        composable(Titans.route) {
            TitansScreen(
                paddingValues = PaddingValues()
            )
        }
        composable(Episodes.route) {
            EpisodesScreen(
                paddingValues = PaddingValues(),
                context = LocalContext.current
            )

        }
        composable(Organizations.route) {
            OrganizationsScreen(
                paddingValues = PaddingValues()
            )

        }
        composable(Locations.route) {
            LocationsScreen(
                paddingValues = PaddingValues()
            )
        }

    }
}