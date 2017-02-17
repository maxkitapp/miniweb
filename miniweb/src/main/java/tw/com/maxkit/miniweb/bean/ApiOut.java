package tw.com.maxkit.miniweb.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class ApiOut {
	private String rcode; // 處理的結果代碼
	private String rdesc; // 處理的結果說明，如果是 4XX, 5XX 的錯誤，就是錯誤的說明
	private String pagename; // 本頁面的 pagename
	private String sessionid; // server 產生的 sessionid，kokola client 只需要根據 server 回傳的本欄位值，回傳給 server，如果一開始沒有這個欄位的值，就回傳空白
	private String returnpage; // return 按鈕在畫面的左上角，回到上一頁的按鈕，要呼叫哪一個 pagename，其 request 也要包含 body 裡面的 input 內容，如果這個欄位是空白的，kokola app 就不要產生 return 按鈕
	private String action; // action 按鈕在畫面的右上角，類似 form action，是下一頁的按鈕要呼叫哪一個 pagename，其 request 也要包含 body 裡面的 input 內容，如果這個欄位是空白的，kokola app 就不要產生 action 按鈕
	private boolean canforward = false; // true/false 是否能將 text 及 image forward 給其他 user/group，只有在本欄位為 true 時才能轉發訊息，其他的狀況都是 false
	private int size = 18; // 字體大小，在本區塊中的文字會套用這個字體大小，格式為: 14
	private String color; // 字體顏色，在本區塊中的文字會套用這個字體顏色，格式為RGB: #FFFFFF
	private String bgcolor; // 背景顏色，在 body 中的文字會套用這個背景顏色，如果 body 裡面的區塊有覆寫 bgcolor，就以該區塊的設定為主，格式為RGB: #FFFFFF
	private String bodyurl; // 如果這裡有網址資料，則會用 webview 頁面，並連結該網址
	private List<Body> body; // 類似 html body，kokola app 會依照 array 的順序，將 body 裡面以類似 table 一行一行的方式，render 到畫面上
	private List<Imgbody> imgbody; // 以 array 的方式，放置圖片的 base64 encoded binary data，每一個圖片的名稱，都可以由 body 中的 image/option 的 imgid 參考到這裡的圖片
	
	public String getRcode() {
		return rcode;
	}
	public void setRcode(String rcode) {
		this.rcode = rcode;
	}
	public String getRdesc() {
		return rdesc;
	}
	public void setRdesc(String rdesc) {
		this.rdesc = rdesc;
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
	public String getReturnpage() {
		return returnpage;
	}
	public void setReturnpage(String returnpage) {
		this.returnpage = returnpage;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public List<Body> getBody() {
		return body;
	}
	public void setBody(List<Body> body) {
		this.body = body;
	}
	public List<Imgbody> getImgbody() {
		return imgbody;
	}
	public void setImgbody(List<Imgbody> imgbody) {
		this.imgbody = imgbody;
	}
	public String getBodyurl() {
		return bodyurl;
	}
	public void setBodyurl(String bodyurl) {
		this.bodyurl = bodyurl;
	}
	public boolean isCanforward() {
		return canforward;
	}
	public void setCanforward(boolean canforward) {
		this.canforward = canforward;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getBgcolor() {
		return bgcolor;
	}
	public void setBgcolor(String bgcolor) {
		this.bgcolor = bgcolor;
	}
}