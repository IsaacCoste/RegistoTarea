package com.example.registotarea.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.registotarea.presentation.home.HomeScreen
import com.example.registotarea.presentation.proyecto.ProyectoListScreen
import com.example.registotarea.presentation.proyecto.ProyectoScreen
import com.example.registotarea.presentation.tarea.TareaListScreen
import com.example.registotarea.presentation.tarea.TareaScreen
import com.example.registotarea.presentation.usuario.UsuarioListScreen
import com.example.registotarea.presentation.usuario.UsuarioScreen

@Composable
fun TareaNavHost(
    navHostController: NavHostController
) {
    NavHost(
        navController = navHostController,
        startDestination = Screen.HomeScreen
    ) {

        composable<Screen.HomeScreen> {
            HomeScreen(navController = navHostController)
        }

        composable<Screen.UsuarioList> {
            UsuarioListScreen(
                createUsuario = {
                    navHostController.navigate(Screen.Usuario(0, false))
                },
                editUsuario = {
                    navHostController.navigate(Screen.Usuario(it, false))
                },
                deleteUsuario = {
                    navHostController.navigate(Screen.Usuario(it, true))
                }
            )
        }
        composable<Screen.Usuario> {
            val args = it.toRoute<Screen.Usuario>()
            UsuarioScreen(
                usuarioId = args.usuarioId,
                goBack = {
                    navHostController.navigateUp()
                },
                isUsuarioDelete = args.isUsuarioDelete
            )
        }
        composable<Screen.ProyectoList> {
            ProyectoListScreen(
                createProyecto = {
                    navHostController.navigate(Screen.Proyecto(0, false))
                },
                editProyecto = {
                    navHostController.navigate(Screen.Proyecto(it, false))
                },
                deleteProyecto = {
                    navHostController.navigate(Screen.Proyecto(it, true))
                }
            )
        }
        composable<Screen.Proyecto> {
            val args = it.toRoute<Screen.Proyecto>()
            ProyectoScreen(
                proyectoId = args.id,
                goBack = {
                    navHostController.navigateUp()
                },
                isProyectoDelete = args.isProyectoDelete
            )
        }
        composable<Screen.TareaList> {
            TareaListScreen(
                createTarea = {
                    navHostController.navigate(Screen.Tarea(0, false))
                },
                editTarea = {
                    navHostController.navigate(Screen.Tarea(it, false))
                },
                deleteTarea = {
                    navHostController.navigate(Screen.Tarea(it, true))
                }
            )
        }
        composable<Screen.Tarea> {
            val args = it.toRoute<Screen.Tarea>()
            TareaScreen(
                tareaId = args.id,
                goBack = {
                    navHostController.navigateUp()
                },
                isTareaDelete = args.isTareaDelete
            )
        }
    }
}