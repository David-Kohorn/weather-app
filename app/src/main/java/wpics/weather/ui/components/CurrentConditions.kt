/**
 * David Kohorn
 */
package wpics.weather.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import wpics.weather.model.weather.CurrentWeather
import wpics.weather.model.weather.UnitSystem
import wpics.weather.ui.theme.OnWeatherText

@Composable
fun CurrentConditions(weather: CurrentWeather, units: UnitSystem) {
    val condition = weather.weather.firstOrNull()
    Column(
        Modifier.fillMaxWidth().padding(top = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(weather.name, style = MaterialTheme.typography.headlineMedium)
        condition?.let {
            WeatherIcon(
                icon = it.icon,
                contentDescription = it.description,
                modifier = Modifier.size(120.dp)
            )
        }
        Text(formatTemp(weather.main.temp, units), style = MaterialTheme.typography.displayLarge)
        Text(
            condition?.description?.replaceFirstChar { c -> c.uppercase() } ?: "",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            "Feels like ${formatTemp(weather.main.feels_like, units)}",
            style = MaterialTheme.typography.bodyLarge,
            color = OnWeatherText.copy(alpha = 0.85f)
        )
    }
}