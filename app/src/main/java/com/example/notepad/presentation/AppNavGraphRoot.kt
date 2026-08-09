package com.example.notepad.presentation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

import com.example.notepad.presentation.notes.MainAppScreen
import com.example.notepad.presentation.create_note.NoteAppCreationScreen
import com.example.notepad.presentation.edit_note.NoteAppEditScreen
import com.example.notepad.presentation.note_view.NoteAppViewScreen
import com.example.notepad.presentation.app_settings.SettingsAppScreen
import com.example.notepad.presentation.common.SCREEN_TRANSITION_DURATION
import com.example.notepad.presentation.navigation.NavigationRoutes
import com.example.notepad.presentation.navigation.Navigator
import com.example.notepad.presentation.create_note.NoteManager

/** Main screen root. */
@Composable
fun AppNavGraphRoot(noteManager: NoteManager) {
    val navController = rememberNavController()
    val navigator = remember { Navigator(navController) }

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = NavigationRoutes.MainScreen.route,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
            popEnterTransition = { EnterTransition.None },
            popExitTransition = { ExitTransition.None }
        ) {
            // main screen
            composable(route = NavigationRoutes.MainScreen.route) {
                MainAppScreen(onNavigateTo = navigator::navigateTo)
            }

            // note creation screen
            composable(
                route = NavigationRoutes.NoteCreationScreen.route,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(SCREEN_TRANSITION_DURATION)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(SCREEN_TRANSITION_DURATION)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(SCREEN_TRANSITION_DURATION)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(SCREEN_TRANSITION_DURATION)
                    )
                }
            ) {
                NoteAppCreationScreen(
                    onNavigateTo = navigator::navigateTo,
                    onAddNote = noteManager::addNote
                )
            }

            // note view screen
            composable(
                route = "${NavigationRoutes.NoteViewScreen.route}/{noteId}",
                arguments = listOf(
                    navArgument("noteId") { type = NavType.LongType }
                ),
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(SCREEN_TRANSITION_DURATION)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(SCREEN_TRANSITION_DURATION)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(SCREEN_TRANSITION_DURATION)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(SCREEN_TRANSITION_DURATION)
                    )
                }
            ) { navBackStackEntry ->
                val noteId = navBackStackEntry.arguments?.getLong("noteId")

                NoteAppViewScreen(
                    noteId = noteId,
                    onNavigateTo = navigator::navigateTo
                )
            }

            // note edit screen
            composable(
                route = "${NavigationRoutes.NoteEditScreen.route}/{noteId}",
                arguments = listOf(
                    navArgument("noteId") { type = NavType.LongType }
                ),
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(SCREEN_TRANSITION_DURATION)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Right,
                        animationSpec = tween(SCREEN_TRANSITION_DURATION)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(SCREEN_TRANSITION_DURATION)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Left,
                        animationSpec = tween(SCREEN_TRANSITION_DURATION)
                    )
                }
            ) { navBackStackEntry ->
                val noteId = navBackStackEntry.arguments?.getLong("noteId")

                NoteAppEditScreen(
                    noteId = noteId,
                    onNavigateTo = navigator::navigateTo
                )
            }

            // settings screen
            composable(
                route = NavigationRoutes.NoteSettingsScreen.route,
                enterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(SCREEN_TRANSITION_DURATION)
                    )
                },
                exitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Down,
                        animationSpec = tween(SCREEN_TRANSITION_DURATION)
                    )
                },
                popEnterTransition = {
                    slideIntoContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(SCREEN_TRANSITION_DURATION)
                    )
                },
                popExitTransition = {
                    slideOutOfContainer(
                        towards = AnimatedContentTransitionScope.SlideDirection.Up,
                        animationSpec = tween(SCREEN_TRANSITION_DURATION)
                    )
                }
            ) { SettingsAppScreen(onNavigateTo = navigator::navigateTo) }
        }
    }
}