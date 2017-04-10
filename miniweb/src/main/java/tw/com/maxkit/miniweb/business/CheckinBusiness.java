package tw.com.maxkit.miniweb.business;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import tw.com.maxkit.miniweb.bean.ApiIn;
import tw.com.maxkit.miniweb.bean.ApiOut;
import tw.com.maxkit.miniweb.bean.Body;
import tw.com.maxkit.miniweb.bean.Imgbody;
import tw.com.maxkit.miniweb.bean.checkin.Checkin;
import tw.com.maxkit.miniweb.common.CommonBusiness;
import tw.com.maxkit.miniweb.utils.DataUtils;

@Service
public class CheckinBusiness extends CommonBusiness {
	private final String API_CHECKIN = "http://localhost:8080/ivrdatasource/checkin/checkinNow";
	private final String API_CHECKOUT = "http://localhost:8080/ivrdatasource/checkin/checkoutNow";
	private final String API_HISTORY = "http://localhost:8080/ivrdatasource/checkin/history";
	
	public ApiOut requestHandler(ApiIn apiIn) {
		String pagename = apiIn.getPagename();

		ApiOut apiOut = new ApiOut();
		switch (pagename) {
		case "home":
			apiOut = homeHandler(apiIn, apiOut);
			break;
		case "checkin":
			apiOut = checkinHandler(apiIn, apiOut);
			break;
		case "checkout":
			apiOut = checkoutHandler(apiIn, apiOut);
			break;
		case "history":
			apiOut = historyHandler(apiIn, apiOut);
			break;

		default:
			break;
		}

		return apiOut;
	}
	
	private ApiOut homeHandler(ApiIn apiIn, ApiOut apiOut) {
		String imgdata = "";
		try {
			imgdata = DataUtils.getPics64img(context, "checkin_home");
		} catch (Exception e) {
			logger.error("Exception:", e);
		}

		Body bodyImg = new Body();
		bodyImg.setType("img");
		bodyImg.setImgid("homepic");

		Body bodyDesc = new Body();
		bodyDesc.setType("span");
		bodyDesc.setValue("透過簽到小程式，可以讓您使用 Kokola 簽到與查詢簽到記錄。");

		Imgbody imgbody = new Imgbody();
		imgbody.setImgid("homepic");
		imgbody.setImgdata(imgdata);

		apiOut.setRcode("200");
		apiOut.setRdesc("ok");
		apiOut.setPagename("home");
		apiOut.setBody(Arrays.asList(bodyImg, bodyDesc));
		apiOut.setImgbody(Arrays.asList(imgbody));

		return apiOut;
	}
	
	private ApiOut checkinHandler(ApiIn apiIn, ApiOut apiOut) {
		String userid = apiIn.getUserid();
		
		// query crm raw data
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("userid", userid);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		
		ResponseEntity<Integer> response= restTemplate.postForEntity(API_CHECKIN, request, Integer.class);
		Integer rcode = response.getBody();
		logger.debug("rcode = {}", rcode);
		
		Body body = new Body();
		body.setType("span");
		body.setSize(24);
		
		if(rcode == 201) {
			body.setValue("您今天稍早已經簽到了。");
			body.setColor("#FFAB00");
			body.setBgcolor("#E6E6FA");
		} else {
			body.setValue("簽到成功。");
			body.setColor("#00D647");
			body.setBgcolor("#E6E6FA");
		}
		
		apiOut.setRcode("200");
		apiOut.setRdesc("ok");
		apiOut.setPagename("checkin");
		apiOut.setReturnpage("home");
		apiOut.setBody(Arrays.asList(body));

		return apiOut;
	}
	
	private ApiOut checkoutHandler(ApiIn apiIn, ApiOut apiOut) {
		String userid = apiIn.getUserid();
		
		// query crm raw data
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("userid", userid);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		
		ResponseEntity<Integer> response= restTemplate.postForEntity(API_CHECKOUT, request, Integer.class);
		Integer rcode = response.getBody();
		logger.debug("rcode = {}", rcode);
		
		Body body = new Body();
		body.setType("span");
		body.setSize(24);
		
		if(rcode == 201) {
			body.setValue("您今天已經簽退了。");
			body.setColor("#FFAB00");
			body.setBgcolor("#E6E6FA");
		} else if(rcode == 203) {
			body.setValue("簽退失敗，您今天尚未簽到。");
			body.setColor("#FFAB00");
			body.setBgcolor("#E6E6FA");
		} else {
			body.setValue("簽退成功。");
			body.setColor("#00D647");
			body.setBgcolor("#E6E6FA");
		}
		
		apiOut.setRcode("200");
		apiOut.setRdesc("ok");
		apiOut.setPagename("checkout");
		apiOut.setReturnpage("home");
		apiOut.setBody(Arrays.asList(body));

		return apiOut;
	}
	
	private ApiOut historyHandler(ApiIn apiIn, ApiOut apiOut) {
		String userid = apiIn.getUserid();
		
		// query crm raw data
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("userid", userid);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
		
		ResponseEntity<Checkin[]> response= restTemplate.postForEntity(API_HISTORY, request, Checkin[].class);
		Checkin[] checkins = response.getBody();
		logger.debug("checkins.length = {}", checkins.length);
		List<Checkin> listCheckin = Arrays.asList(checkins);
		List<Body> bodys = DataUtils.checkinsToBodys(listCheckin);
		
		apiOut.setRcode("200");
		apiOut.setRdesc("ok");
		apiOut.setPagename("history");
		apiOut.setReturnpage("home");
		apiOut.setBody(bodys);

		return apiOut;
	}
}
