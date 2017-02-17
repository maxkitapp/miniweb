package tw.com.maxkit.miniweb.bean.cwb;

public class Time {
	private String startTime; // "2017-01-12 12:00:00",
	private String endTime; //2017-01-12 15:00:00",
	private String elementValue; //"多雲。 降雨機率 10%。 溫度攝氏24度。 舒適。 偏北風 平均風速3至4級(每秒6公尺)。 相對濕度為66%。"
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getElementValue() {
		return elementValue;
	}
	public void setElementValue(String elementValue) {
		this.elementValue = elementValue;
	}
}
