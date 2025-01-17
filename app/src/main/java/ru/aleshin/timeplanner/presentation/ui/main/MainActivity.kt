/*
 * Copyright 2023 Stanislav Aleshin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ru.aleshin.timeplanner.presentation.ui.main

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import cafe.adriel.voyager.navigator.CurrentScreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.aleshin.core.ui.theme.TimePlannerTheme
import ru.aleshin.core.utils.navigation.navigator.AppNavigator
import ru.aleshin.core.utils.navigation.navigator.NavigatorManager
import ru.aleshin.core.utils.platform.activity.BaseActivity
import ru.aleshin.core.utils.platform.screen.ScreenContent
import ru.aleshin.features.home.api.navigation.HomeScreens
import ru.aleshin.timeplanner.application.fetchApp
import ru.aleshin.timeplanner.di.annotation.GlobalNavigation
import ru.aleshin.timeplanner.presentation.ui.main.contract.MainAction
import ru.aleshin.timeplanner.presentation.ui.main.contract.MainEffect
import ru.aleshin.timeplanner.presentation.ui.main.contract.MainEvent
import ru.aleshin.timeplanner.presentation.ui.main.contract.MainViewState
import ru.aleshin.timeplanner.presentation.ui.main.viewmodel.MainViewModel
import ru.aleshin.timeplanner.presentation.ui.splash.SplashScreen
import ru.aleshin.timeplanner.presentation.ui.tabs.TabsScreen
import javax.inject.Inject

class MainActivity : BaseActivity<MainViewState, MainEvent, MainAction, MainEffect>() {

    @Inject
    @GlobalNavigation
    lateinit var navigatorManager: NavigatorManager

    @Inject
    lateinit var viewModelFactory: MainViewModel.Factory

    override fun initDI() = fetchApp().appComponent.inject(this)

    @Composable
    override fun Content() = ScreenContent(viewModel, MainViewState()) { state ->
        TimePlannerTheme(
            dynamicColor = state.isEnableDynamicColors,
            themeType = state.colors,
            languageType = state.language,
        ) {
            AppNavigator(
                initialScreen = SplashScreen(),
                navigatorManager = navigatorManager,
                content = {
                    CurrentScreen()
                    if (it.lastItem is TabsScreen) getNotificationPermission()
                },
            )
        }


    }

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            Log.e("Notification_Permission", "Notification Permission Allowed")
        } else {
            Log.e("Notification_Permission", "Notification Permission Denied")
        }
    }


    private fun getNotificationPermission() {
        try {
            if (Build.VERSION.SDK_INT > 32) requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    override fun fetchViewModelFactory() = viewModelFactory

    override fun fetchViewModelClass() = MainViewModel::class.java
}
