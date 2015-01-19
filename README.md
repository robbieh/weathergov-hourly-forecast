# weathergov-hourly-forecast

This simply fetches the hourly forecast grid available at weather.gov, parses it, and puts the data in a hash-map.

## Usage

For your project.clj:
	[weathergov-hourly-forecast "0.0.2"]

For your code:
	(:use [weathergov-hourly-forecast.core :only get-forecast-table pivot-data])

Call the get-forecast-data function with latitude and longitude coordiates for the spot you want.

This returns a hash-map with keys such as:

	(:TemperatureF :DewpointF :SkyCover% :RelativeHumidity% :Thunder :WindDir :PrecipitationPotential% :Rain :WindChillF :FreezingRain :Gust :Sleet :SurfaceWindmph :Date :Snow)

If you would rather have the data indxed by date and hour, call (pivot-data) on the table.

## Disclaimer

This is 0.0.1 level code. No errors are caught. The parsing could be more efficient. 


