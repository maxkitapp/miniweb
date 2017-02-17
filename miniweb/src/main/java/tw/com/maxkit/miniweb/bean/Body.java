package tw.com.maxkit.miniweb.bean;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class Body {
	private String type; // 欄位類型，hidden、text、textarea、option、option-grid、checkbox、checkbox-grid、span、img
	private String id; // 欄位id，如果是 input/text, input/textarea, input/option, input/checkbox 的資料，這個欄位就是該 input 欄位，要發送 request 時的 id，但如果只是一般文字或畫面顯示的區塊，則沒有作用
	private String value; // 欄位名稱，如果是 input/text, input/textarea, input/option, input/checkbox 的資料，這個欄位就是該 input 欄位在畫面上顯示的名稱。如果 type 為 span 只是一般文字顯示的區塊，則是文字區塊的內容。如果 type 為 hidden，則是輸入的資料，在後續的 request 都直接回傳即可。
	private String keyboard; // 鍵盤的類型，在 input/text 的輸入中，指定該輸入資料的鍵盤類型
	private List<Option> optionlist; // 當 type 為 option/checkbox 時，以此設定該 option/checkbox 的選項
	private int size; // 字體大小，在本區塊中的文字會套用這個字體大小，格式為: 14
	private String color; // 字體顏色，在本區塊中的文字會套用這個字體顏色，格式為RGB: #FFFFFF
	private String bgcolor; // 背景顏色，在本區塊中的文字會套用這個背景顏色，格式為RGB: #FFFFFF
	private String imgid; // 當 type 為 img 時，以此 imgid 參考到下面 imgbody 裡面的圖片
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getKeyboard() {
		return keyboard;
	}
	public void setKeyboard(String keyboard) {
		this.keyboard = keyboard;
	}
	public List<Option> getOptionlist() {
		return optionlist;
	}
	public void setOptionlist(List<Option> optionlist) {
		this.optionlist = optionlist;
	}
	public String getImgid() {
		return imgid;
	}
	public void setImgid(String imgid) {
		this.imgid = imgid;
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
