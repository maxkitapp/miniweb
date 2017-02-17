package tw.com.maxkit.miniweb.bean.cwb;

import java.util.List;

public class Result {
	private String resource_id;
	private List<Field> fields;
	public String getResource_id() {
		return resource_id;
	}
	public void setResource_id(String resource_id) {
		this.resource_id = resource_id;
	}
	public List<Field> getFields() {
		return fields;
	}
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
}
