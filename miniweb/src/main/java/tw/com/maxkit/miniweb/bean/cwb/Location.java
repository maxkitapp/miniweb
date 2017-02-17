package tw.com.maxkit.miniweb.bean.cwb;

import java.util.List;

public class Location {
	private String locationName ;// "西屯區"
	private String geocode; // "6600600",
	private String lat; // "24.1673",
	private String lon; // "120.626",
	private List<WeatherElement> weatherElement;
	public String getLocationName() {
		return locationName;
	}
	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}
	public String getGeocode() {
		return geocode;
	}
	public void setGeocode(String geocode) {
		this.geocode = geocode;
	}
	public String getLat() {
		return lat;
	}
	public void setLat(String lat) {
		this.lat = lat;
	}
	public String getLon() {
		return lon;
	}
	public void setLon(String lon) {
		this.lon = lon;
	}
	public List<WeatherElement> getWeatherElement() {
		return weatherElement;
	}
	public void setWeatherElement(List<WeatherElement> weatherElement) {
		this.weatherElement = weatherElement;
	}
}
