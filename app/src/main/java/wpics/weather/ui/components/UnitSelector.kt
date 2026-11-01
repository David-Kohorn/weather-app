package wpics.weather.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import wpics.weather.model.weather.UnitSystem
import wpics.weather.ui.theme.OnWeatherSelected
import wpics.weather.ui.theme.OnWeatherText

@Composable
fun UnitSelector(selected: UnitSystem, onUnitChange: (UnitSystem) -> Unit) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        listOf(UnitSystem.METRIC, UnitSystem.IMPERIAL, UnitSystem.STANDARD).forEach { unit ->
            if (unit == selected) {
                Button(
                    onClick = { onUnitChange(unit) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = OnWeatherText,
                        contentColor = OnWeatherSelected
                    )
                ) { Text(unit.display, maxLines = 1) }
            } else {
                OutlinedButton(
                    onClick = { onUnitChange(unit) },
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = OnWeatherText),
                    border = BorderStroke(1.dp, OnWeatherText.copy(alpha = 0.7f))
                ) { Text(unit.display, maxLines = 1) }
            }
        }
    }
}