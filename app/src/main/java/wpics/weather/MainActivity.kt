/**
 * David Kohorn
 */
package wpics.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import wpics.weather.model.location.PlayServicesLocationRepository
import wpics.weather.model.weather.WeatherRepository
import wpics.weather.ui.WeatherScreen
import wpics.weather.ui.theme.WeatherTheme
import wpics.weather.viewmodel.LocationViewModel
import wpics.weather.viewmodel.WeatherViewModel

/**
 * This is the main entry activity for our weather application.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val locationRepo = PlayServicesLocationRepository(applicationContext)
        val weatherRepo = WeatherRepository()

        val appFactory = viewModelFactory {
            initializer {
                LocationViewModel(locationRepo)
            }
            initializer {
                WeatherViewModel(weatherRepo)
            }
        }

        enableEdgeToEdge()
        setContent {
            WeatherTheme {
                val locViewModel: LocationViewModel =
                    viewModel(factory = appFactory)
                val discoverViewModel: WeatherViewModel =
                    viewModel(factory = appFactory)
                WeatherScreen(
                    locViewModel = locViewModel,
                    discoverViewModel = discoverViewModel
                )
            }
        }
    }
}
