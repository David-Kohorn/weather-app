/**
 * David Kohorn
 */
package wpics.weather.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage

private fun iconUrl(icon: String): String = "https://openweathermap.org/img/wn/${icon}@2x.png"

@Composable
fun WeatherIcon(
    icon: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    if (icon.isNullOrBlank()) {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "—",
                style = MaterialTheme.typography.bodyMedium
            )
        }
        return
    }

    AsyncImage(
        model = iconUrl(icon),
        contentDescription = contentDescription,
        modifier = modifier
    )
}