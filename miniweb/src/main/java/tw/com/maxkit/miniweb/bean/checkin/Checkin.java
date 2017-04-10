package tw.com.maxkit.miniweb.bean.checkin;

import java.util.Date;

public class Checkin {
	private Long seq;
	private String userid;
	private Date checkindate;
	private Date checkoutdate;
	
	public Long getSeq() {
		return seq;
	}
	public void setSeq(Long seq) {
		this.seq = seq;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public Date getCheckindate() {
		return checkindate;
	}
	public void setCheckindate(Date checkindate) {
		this.checkindate = checkindate;
	}
	public Date getCheckoutdate() {
		return checkoutdate;
	}
	public void setCheckoutdate(Date checkoutdate) {
		this.checkoutdate = checkoutdate;
	}
}