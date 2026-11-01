/**
 * David Kohorn
 */
package wpics.weather.model.weather

enum class UnitSystem(val api: String, val display: String) {
    STANDARD("standard", "Standard"),
    IMPERIAL("imperial", "Imperial"),
    METRIC("metric", "Metric"),
}