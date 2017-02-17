package tw.com.maxkit.miniweb.bean.cwb;

import java.util.List;

public class Records {
	private String contentDescription; // "臺灣各鄉鎮市區未來2天(逐3小時)及未來1週天氣預報"
	private List<Locations> locations;
	public String getContentDescription() {
		return contentDescription;
	}
	public void setContentDescription(String contentDescription) {
		this.contentDescription = contentDescription;
	}
	public List<Locations> getLocations() {
		return locations;
	}
	public void setLocations(List<Locations> locations) {
		this.locations = locations;
	}
}
