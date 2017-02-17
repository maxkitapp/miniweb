package tw.com.maxkit.miniweb.bean.cwb;

import java.util.List;

public class Locations {
	private String datasetDescription; //: "臺中市未來2天天氣預報",
	private String locationsName; //臺中市
	private String dataid; //D0047-073"
	private List<Location> location;
	public String getDatasetDescription() {
		return datasetDescription;
	}
	public void setDatasetDescription(String datasetDescription) {
		this.datasetDescription = datasetDescription;
	}
	public String getLocationsName() {
		return locationsName;
	}
	public void setLocationsName(String locationsName) {
		this.locationsName = locationsName;
	}
	public String getDataid() {
		return dataid;
	}
	public void setDataid(String dataid) {
		this.dataid = dataid;
	}
	public List<Location> getLocation() {
		return location;
	}
	public void setLocation(List<Location> location) {
		this.location = location;
	}
}
