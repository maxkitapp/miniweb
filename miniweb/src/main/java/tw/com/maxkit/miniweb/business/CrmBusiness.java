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
import tw.com.maxkit.miniweb.bean.Postdata;
import tw.com.maxkit.miniweb.bean.crm.Contact;
import tw.com.maxkit.miniweb.common.CommonBusiness;
import tw.com.maxkit.miniweb.utils.DataUtils;
import tw.com.maxkit.miniweb.utils.session.SessionManager;

@Service
public class CrmBusiness extends CommonBusiness {
	private final String API_SEARCH_LASTNAME = "http://localhost:8080/ivrdatasource/crm/searchbylastname";
	private final String API_SEARCH_ACCNAME = "http://localhost:8080/ivrdatasource/crm/searchbyaccname";
	private final String API_GETBYID = "http://localhost:8080/ivrdatasource/crm/getbyid";

	public ApiOut requestHandler(ApiIn apiIn) {
		String pagename = apiIn.getPagename();
		String userid = apiIn.getUserid();
		String sessionid = apiIn.getSessionid();

		SessionManager sessionManager = SessionManager.getInstance();

		ApiOut apiOut = new ApiOut();
		switch (pagename) {
		case "home":
			apiOut = homeHandler(apiIn, apiOut);
			break;
		case "byname":
			sessionid = sessionManager.initSession(userid);
			apiOut = bynameHandler(apiIn, apiOut);
			break;
		case "byaccname":
			sessionid = sessionManager.initSession(userid);
			apiOut = byaccnameHandler(apiIn, apiOut);
			break;
		case "searchbyname":
			apiIn = sessionManager.sessionHandler(apiIn);
			apiOut = searchbynameHandler(apiIn, apiOut);
			break;
		case "searchbyaccname":
			apiIn = sessionManager.sessionHandler(apiIn);
			apiOut = searchbyaccnameHandler(apiIn, apiOut);
			break;
		case "getbyid_name":
		case "getbyid_accname":
			apiOut = getbyidHandler(apiIn, apiOut);
			break;

		default:
			break;
		}

		apiOut.setSessionid(sessionid);
		return apiOut;
	}

	private ApiOut homeHandler(ApiIn apiIn, ApiOut apiOut) {
		String imgdata = "";
		try {
			imgdata = DataUtils.getPics64img(context, "crm_home");
		} catch (Exception e) {
			logger.error("Exception:", e);
		}

		Body bodyImg = new Body();
		bodyImg.setType("img");
		bodyImg.setImgid("homepic");

		Body bodyDesc = new Body();
		bodyDesc.setType("span");
		bodyDesc.setValue("透過 CRM 小程式，可以使用客戶姓名或公司名稱搜尋相關客戶資訊。");

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

	private ApiOut bynameHandler(ApiIn apiIn, ApiOut apiOut) {
		Body body = new Body();
		body.setId("name");
		body.setType("text");
		body.setValue("請輸入人員名稱");
		body.setKeyboard("any");

		apiOut.setRcode("200");
		apiOut.setRdesc("ok");
		apiOut.setPagename("byname");
		apiOut.setReturnpage("home");
		apiOut.setAction("searchbyname");
		apiOut.setBody(Arrays.asList(body));

		return apiOut;
	}

	private ApiOut searchbynameHandler(ApiIn apiIn, ApiOut apiOut) {
		Postdata postdata = apiIn.getPostdata().get(0);
		String postId = postdata.getId(); // name
		String postValue = postdata.getValue();
		logger.debug("getpost, postId = {}, postValue = {}", postId, postValue);

		// query crm raw data
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("lastname", postValue);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

		ResponseEntity<Contact[]> response = restTemplate.postForEntity(API_SEARCH_LASTNAME, request, Contact[].class);
		Contact[] contacts = response.getBody();
		List<Contact> listContacts = Arrays.asList(contacts);
		logger.debug("l = {}", listContacts.size());
		List<Body> bodys = DataUtils.contactsToBody(postValue, listContacts);

		apiOut.setRcode("200");
		apiOut.setRdesc("ok");
		apiOut.setPagename("searchbyname");
		apiOut.setReturnpage("byaccname");
		apiOut.setAction("getbyid_name");
		apiOut.setBody(bodys);
		return apiOut;
	}

	private ApiOut getbyidHandler(ApiIn apiIn, ApiOut apiOut) {
		String pagename = apiIn.getPagename();
		Postdata postdata = apiIn.getPostdata().get(0);
		String postId = postdata.getId(); // cid
		String postValue = postdata.getValue();
		logger.debug("getpost, postId = {}, postValue = {}", postId, postValue);

		// query cwb raw data
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("id", postValue);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

		ResponseEntity<Contact> response = restTemplate.postForEntity(API_GETBYID, request, Contact.class);
		Contact contact = response.getBody();
		List<Body> bodys = DataUtils.contactToBody(contact);

		apiOut.setRcode("200");
		apiOut.setRdesc("ok");
		apiOut.setPagename(pagename);
		if (pagename.equals("getbyid_name")) {
			apiOut.setReturnpage("searchbyname");
		} else if (pagename.equals("getbyid_accname")) {
			apiOut.setReturnpage("searchbyaccname");
		}
		apiOut.setCanforward(true);
		apiOut.setBody(bodys);
		return apiOut;
	}

	private ApiOut byaccnameHandler(ApiIn apiIn, ApiOut apiOut) {
		Body body = new Body();
		body.setId("accname");
		body.setType("text");
		body.setValue("請輸入搜尋公司名稱");
		body.setKeyboard("any");

		apiOut.setRcode("200");
		apiOut.setRdesc("ok");
		apiOut.setPagename("byaccname");
		apiOut.setReturnpage("home");
		apiOut.setAction("searchbyaccname");
		apiOut.setBody(Arrays.asList(body));

		return apiOut;
	}

	private ApiOut searchbyaccnameHandler(ApiIn apiIn, ApiOut apiOut) {
		Postdata postdata = apiIn.getPostdata().get(0);
		String postId = postdata.getId(); // accname
		String postValue = postdata.getValue();
		logger.debug("getpost, postId = {}, postValue = {}", postId, postValue);

		// query cwb raw data
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("accname", postValue);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

		ResponseEntity<Contact[]> response = restTemplate.postForEntity(API_SEARCH_ACCNAME, request, Contact[].class);
		Contact[] contacts = response.getBody();
		List<Contact> listContacts = Arrays.asList(contacts);
		// convert cwb raw data to response format
		List<Body> bodys = DataUtils.contactsToBody(postValue, listContacts);

		apiOut.setRcode("200");
		apiOut.setRdesc("ok");
		apiOut.setPagename("searchbyname");
		apiOut.setReturnpage("byaccname");
		apiOut.setAction("getbyid_accname");
		apiOut.setBody(bodys);
		return apiOut;
	}
}