/**
 * David Kohorn
 */
package wpics.weather.ui.theme

import androidx.compose.ui.graphics.Color

// ---- Material color scheme (light) ----
val LightPrimary = Color(0xFF1E6FD9)
val LightOnPrimary = Color(0xFFFFFFFF)
val LightBackground = Color(0xFFF4F8FC)
val LightOnBackground = Color(0xFF0F1B2D)
val LightError = Color(0xFFB3261E)
val LightOnError = Color(0xFFFFFFFF)

// ---- Material color scheme (dark) ----
val DarkPrimary = Color(0xFF8AB8FF)
val DarkOnPrimary = Color(0xFF06264F)
val DarkBackground = Color(0xFF0E141B)
val DarkOnBackground = Color(0xFFE6EDF5)
val DarkError = Color(0xFFFFB4AB)
val DarkOnError = Color(0xFF690005)

// ---- Text and cards drawn on top of the weather backgrounds ----
// Every background below is dark enough for white text (contrast >= 4.5:1),
// and the card overlay only darkens further.
val OnWeatherText = Color(0xFFFFFFFF)
val OnWeatherSelected = Color(0xFF0F1B2D) // text on the selected (white) unit button
val WeatherCardOverlay = Color(0x40000000)
const val DarkThemeDim = 0.35f // how much to darken backgrounds in dark theme

// ---- Weather backgrounds: top / bottom of a vertical gradient ----
val ClearDayTop = Color(0xFF1E6FD9);    val ClearDayBottom = Color(0xFF0B4FA8)
val ClearNightTop = Color(0xFF1B2A5E);  val ClearNightBottom = Color(0xFF0B1230)

val CloudsDayTop = Color(0xFF5C6F85);   val CloudsDayBottom = Color(0xFF3E4C5E)
val CloudsNightTop = Color(0xFF2E3440); val CloudsNightBottom = Color(0xFF171B24)

val RainDayTop = Color(0xFF3B5568);     val RainDayBottom = Color(0xFF243645)
val RainNightTop = Color(0xFF1C2733);   val RainNightBottom = Color(0xFF0E141B)

val StormDayTop = Color(0xFF3D3556);    val StormDayBottom = Color(0xFF1F1A33)
val StormNightTop = Color(0xFF241E3A);  val StormNightBottom = Color(0xFF0F0B1D)

val SnowDayTop = Color(0xFF4F6F8F);     val SnowDayBottom = Color(0xFF35506B)
val SnowNightTop = Color(0xFF2A3A4D);   val SnowNightBottom = Color(0xFF161F2B)

val MistDayTop = Color(0xFF6B7280);     val MistDayBottom = Color(0xFF4B5160)
val MistNightTop = Color(0xFF30343C);   val MistNightBottom = Color(0xFF1A1C21)