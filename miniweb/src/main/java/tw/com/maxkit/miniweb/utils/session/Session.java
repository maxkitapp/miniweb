package tw.com.maxkit.miniweb.utils.session;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tw.com.maxkit.miniweb.bean.Postdata;

public class Session {
	private String sessionid;
	private Map<String, List<Postdata>> flow = new HashMap<>();
	
	public Session(String sessionid) {
		this.sessionid = sessionid;
	}
	
	public String getSessionid() {
		return sessionid;
	}

	public Map<String, List<Postdata>> getFlow() {
		return flow;
	}
}