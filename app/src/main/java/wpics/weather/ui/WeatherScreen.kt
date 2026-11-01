/**
 * David Kohorn
 */
package wpics.weather.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import wpics.weather.model.weather.CurrentWeather
import wpics.weather.model.weather.UnitSystem
import wpics.weather.ui.components.CurrentConditions
import wpics.weather.ui.components.DetailsCard
import wpics.weather.ui.components.ForecastCard
import wpics.weather.ui.components.HourlyCard
import wpics.weather.ui.components.UnitSelector
import wpics.weather.ui.theme.OnWeatherText
import wpics.weather.ui.theme.weatherBackgroundBrush
import wpics.weather.viewmodel.LocationViewModel
import wpics.weather.viewmodel.WeatherUIState
import wpics.weather.viewmodel.WeatherViewModel

/**
 * Stateful weather screen. Requests location permission, gets the location,
 * and asks the [WeatherViewModel] to fetch the weather for it.
 */
@Composable
fun WeatherScreen(
    locViewModel: LocationViewModel,
    discoverViewModel: WeatherViewModel,
    modifier: Modifier = Modifier
) {
    val weatherState by discoverViewModel.uiState.collectAsStateWithLifecycle()
    val locState by locViewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    // Result of the runtime permission dialog
    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        locViewModel.onPermissionResult(granted)
        if (granted) locViewModel.fetchLocation()
    }

    // On first launch: use the permission if we have it, otherwise ask for it.
    // rememberSaveable stops us from re-prompting after a rotation.
    var askedForPermission by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        if (askedForPermission) return@LaunchedEffect
        askedForPermission = true
        val alreadyGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (alreadyGranted) {
            locViewModel.onPermissionResult(true)
            locViewModel.fetchLocation()
        } else {
            launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    // Once a location is available, fetch the weather (skipped if already fetched, e.g. after rotation)
    LaunchedEffect(locState.coordinate) {
        val coordinate = locState.coordinate ?: return@LaunchedEffect
        if (coordinate != discoverViewModel.uiState.value.coordinate) {
            discoverViewModel.fetchData(coordinate)
        }
    }

    WeatherScreenContent(
        weatherState = weatherState,
        permissionGranted = locState.permissionGranted,
        locationLoading = locState.isLoading,
        hasLocation = locState.coordinate != null,
        onRequestPermission = { launcher.launch(Manifest.permission.ACCESS_FINE_LOCATION) },
        onOpenSettings = {
            context.startActivity(
                Intent(
                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                    Uri.fromParts("package", context.packageName, null)
                )
            )
        },
        onRetryLocation = { locViewModel.fetchLocation() },
        onRetryWeather = { weatherState.coordinate?.let { discoverViewModel.fetchData(it) } },
        onUnitChange = discoverViewModel::updateUnitSystem,
        modifier = modifier
    )
}

/**
 * Stateless screen content. Chooses between the permission, loading, error,
 * and weather views and draws the condition-based background.
 */
@Composable
fun WeatherScreenContent(
    weatherState: WeatherUIState,
    permissionGranted: Boolean,
    locationLoading: Boolean,
    hasLocation: Boolean,
    onRequestPermission: () -> Unit,
    onOpenSettings: () -> Unit,
    onRetryLocation: () -> Unit,
    onRetryWeather: () -> Unit,
    onUnitChange: (UnitSystem) -> Unit,
    modifier: Modifier = Modifier
) {
    val weather = weatherState.currentWeather
    val showWeather = weather != null && weatherState.error == null
    val icon = weather?.weather?.firstOrNull()?.icon

    // Weather-based gradient when we have data, otherwise the normal theme background
    val background: Brush = if (showWeather) {
        weatherBackgroundBrush(icon, isSystemInDarkTheme())
    } else {
        SolidColor(MaterialTheme.colorScheme.background)
    }
    val contentColor = if (showWeather) OnWeatherText else MaterialTheme.colorScheme.onBackground

    Box(modifier.fillMaxSize().background(background)) {
        CompositionLocalProvider(LocalContentColor provides contentColor) {
            when {
                !permissionGranted -> MessageScreen(
                    title = "Location permission needed",
                    message = "This app uses your location to show your local weather. " +
                            "If the permission prompt doesn't appear, enable it in settings.",
                    primaryLabel = "Grant permission",
                    onPrimary = onRequestPermission,
                    secondaryLabel = "Open settings",
                    onSecondary = onOpenSettings
                )

                weatherState.error != null -> MessageScreen(
                    title = "Couldn't load the weather",
                    message = "${weatherState.error}. Check your connection and try again.",
                    primaryLabel = "Retry",
                    onPrimary = onRetryWeather
                )

                showWeather -> WeatherContent(weatherState, weather!!, onUnitChange)

                weatherState.isLoading || locationLoading -> LoadingScreen(
                    if (locationLoading) "Finding your location…" else "Loading weather…"
                )

                !hasLocation -> MessageScreen(
                    title = "Location unavailable",
                    message = "We couldn't determine your location. Make sure location " +
                            "services are turned on, then try again.",
                    primaryLabel = "Try again",
                    onPrimary = onRetryLocation
                )

                else -> LoadingScreen("Loading weather…")
            }
        }
    }
}

@Composable
private fun WeatherContent(
    state: WeatherUIState,
    weather: CurrentWeather,
    onUnitChange: (UnitSystem) -> Unit
) {
    val units = state.unitSystem

    Box(Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { CurrentConditions(weather, units) }
            item { HourlyCard(state.currentForecast, units) }
            item { DetailsCard(weather, units) }
            item { ForecastCard(state.currentForecast, units) }
            item { UnitSelector(units, onUnitChange) }
        }

        // Thin progress bar while refreshing (e.g. after switching units)
        if (state.isLoading) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth().safeDrawingPadding().align(Alignment.TopCenter),
                color = OnWeatherText,
                trackColor = OnWeatherText.copy(alpha = 0.3f)
            )
        }
    }
}

