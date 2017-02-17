package tw.com.maxkit.miniweb.utils.session;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.util.StringUtils;

import tw.com.maxkit.miniweb.bean.ApiIn;
import tw.com.maxkit.miniweb.bean.Postdata;

public class SessionManager {

	private volatile static SessionManager instance;

	private SessionManager() {
	};

	public static SessionManager getInstance() {
		if (instance == null) {
			synchronized (SessionManager.class) {
				if (instance == null) {
					instance = new SessionManager();
				}
			}
		}
		return instance;
	}

	private Map<String, Session> sessionStore = new ConcurrentHashMap<>(); // key:userid,

	public ApiIn sessionHandler(ApiIn apiIn) {
		String userid = apiIn.getUserid();
		String sessionid = apiIn.getSessionid();
		String pagename = apiIn.getPagename();
		List<Postdata> postdata = apiIn.getPostdata();
		
		if(postdata == null || postdata.size() == 0) {
			postdata = rollbackFlow(userid, sessionid, pagename);
			apiIn.setPostdata(postdata);
		} else {
			addFlow(userid, sessionid, pagename, postdata);
		}
		
		return apiIn;
	}

	public String initSession(String userid) {
		String sessionid = UUID.randomUUID().toString();
		Session session = new Session(sessionid);
		sessionStore.put(userid, session);
		return sessionid;
	}
	
	private void addFlow(String userid, String sessionid, String pagename, List<Postdata> postdatas) {
		if (isvalid(userid, sessionid)) {
			Session session = sessionStore.get(userid);
			Map<String, List<Postdata>> flow = session.getFlow();
			flow.put(pagename, postdatas);
		}
	}

	private List<Postdata> rollbackFlow(String userid, String sessionid, String pagename) {
		List<Postdata> postdatas = null;
		if (isvalid(userid, sessionid)) {
			Session session = sessionStore.get(userid);
			Map<String, List<Postdata>> flow = session.getFlow();
			postdatas = flow.get(pagename);
		}
		return postdatas;
	}

	private boolean isvalid(String userid, String sessionid) {
		if (StringUtils.isEmpty(userid)) {
			return false; // userid empty
		}

		if (StringUtils.isEmpty(sessionid)) {
			return false; // sessionid empty
		}

		Session session = sessionStore.get(userid);
		if (session == null) {
			return false; // not found
		}

		if (!sessionid.equals(session.getSessionid())) {
			return false; // not same session
		}

		return true;
	}
}
