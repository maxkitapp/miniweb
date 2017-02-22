package tw.com.maxkit.miniweb.bean;

import java.util.List;

public class ApiIn {
	private String userid; // 使用者 id
	private String exectoken; // 驗證碼
	private String pagename; // ex: home, case, case-step1, case-step2, board ....
	private String sessionid; // server 產生的 sessionid，kokola client 只需要根據 server 回傳的本欄位值，回傳給 server，如果一開始沒有這個欄位的值，就回傳空白
	private List<Postdata> postdata; // 類似 html form post，將畫面上所有的 input 資料，以成對的 id, value 的方式，回傳給 server。如果 input 為多選，則以 { "id" : "location", "value" : "160"}, { "id" : "location", "value" : "120"} 多個相同的 id 這種方式傳送
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getExectoken() {
		return exectoken;
	}
	public void setExectoken(String exectoken) {
		this.exectoken = exectoken;
	}
	public String getPagename() {
		return pagename;
	}
	public void setPagename(String pagename) {
		this.pagename = pagename;
	}
	public String getSessionid() {
		return sessionid;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public List<Postdata> getPostdata() {
		return postdata;
	}
	public void setPostdata(List<Postdata> postdata) {
		this.postdata = postdata;
	}
}
