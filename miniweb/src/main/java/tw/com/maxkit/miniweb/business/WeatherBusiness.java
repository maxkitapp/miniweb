package tw.com.maxkit.miniweb.business;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Base64;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import net.coobird.thumbnailator.Thumbnails;
import tw.com.maxkit.miniweb.bean.ApiIn;
import tw.com.maxkit.miniweb.bean.ApiOut;
import tw.com.maxkit.miniweb.bean.Body;
import tw.com.maxkit.miniweb.bean.Imgbody;
import tw.com.maxkit.miniweb.bean.Postdata;
import tw.com.maxkit.miniweb.bean.cwb.CwbDataStore;
import tw.com.maxkit.miniweb.common.CommonBusiness;
import tw.com.maxkit.miniweb.utils.DataUtils;

@Service
public class WeatherBusiness extends CommonBusiness {
	private final String IMG_SRC_FOLDER_NAME = "ivrpic";
	private final String IMG_THUMB_FOLDER_NAME = "ivrpicthumb";
	private final String IMG_NAME = "cwbSatellite.jpg";
	private final int IMG_SIZE = 300;
	private final String API_GETWEATHER_URL = "http://localhost:8080/ivrdatasource/cwb/getWeather";
	
	public ApiOut requestHandler(ApiIn apiIn) {
		String pagename = apiIn.getPagename();
		logger.debug("into requestHandler, pathname = {}", pagename);
		
		ApiOut apiOut = new ApiOut();
		
		switch (pagename) {
		case "home":
			apiOut = homeHandler(apiIn, apiOut);
			break;
		case "satellite":
			apiOut = satelliteHandler(apiIn, apiOut);
			break;
		case "taichung":
		case "kaohsiung":
		case "taipei":
			apiOut = getWeatherHandler(apiIn, apiOut, pagename);
			break;
		case "listEntertainment":
			apiOut = listEntertainmentHandler(apiIn, apiOut);
			break;
		case "queryEntertainment":
			apiOut = queryEntertainmentHandler(apiIn, apiOut, pagename);
			break;
		default:
			logger.debug("unknow pagename {}", pagename);
			apiOut = defaultHandler(apiIn, apiOut);
			break;
		}
		
		return apiOut;
	}
	private ApiOut listEntertainmentHandler(ApiIn apiIn, ApiOut apiOut) {
		List<Body> bodys = DataUtils.generateEntertainmentBodys();
		
		apiOut.setRcode("200");
		apiOut.setRdesc("ok");
		apiOut.setPagename("listEntertainment");
		apiOut.setReturnpage("home");
		apiOut.setAction("queryEntertainment");
		apiOut.setBody(bodys);
		return apiOut;
	}
	
	private ApiOut queryEntertainmentHandler(ApiIn apiIn, ApiOut apiOut, String pagename) {
		Postdata postdata = apiIn.getPostdata().get(0);
		String postId = postdata.getId(); // eid
		String postValue = postdata.getValue();
		
		logger.debug("getpost, postId = {}, postValue = {}", postId, postValue);
		
		String bodyurl = "";
		
		switch (postValue) {
		case "fishing":
			bodyurl = "http://www.cwb.gov.tw/m/f/entertainment/B011.php";
			break;
		case  "biking" :
			bodyurl = "http://www.cwb.gov.tw/m/f/entertainment/C047.php";
			break;
		case "stargazing":
			bodyurl = "http://www.cwb.gov.tw/m/f/entertainment/F010.php";
			break;
		case "hiking":
			bodyurl = "http://www.cwb.gov.tw/m/f/entertainment/D115.php";
			break;
		case "traveling":
			bodyurl = "http://www.cwb.gov.tw/m/f/entertainment/E029.php";
			break;
		default:
			break;
		}
		
		apiOut.setRcode("200");
		apiOut.setRdesc("ok");
		apiOut.setPagename(pagename);
		apiOut.setBodyurl(bodyurl);
		apiOut.setReturnpage("listEntertainment");
		
		return apiOut;
	}
	
	private ApiOut homeHandler(ApiIn apiIn, ApiOut apiOut) {
		Body body = new Body();
		body.setType("span");
		body.setValue("Welcome to Weather App");
		
		apiOut.setRcode("200");
		apiOut.setRdesc("ok");
		apiOut.setPagename("home");
		apiOut.setBody(Arrays.asList(body));
		
		return apiOut;
	}
	
	private ApiOut defaultHandler(ApiIn apiIn, ApiOut apiOut) {
		apiOut.setRcode("404");
		apiOut.setRdesc("Pagename " + apiIn.getPagename() + " not found");
		return apiOut;
	}
	
	private ApiOut getWeatherHandler(ApiIn apiIn, ApiOut apiOut, String pagename) {
		// query cwb raw data
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

		MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
		map.add("area", pagename);

		HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

		ResponseEntity<CwbDataStore> response = restTemplate.postForEntity(API_GETWEATHER_URL, request , CwbDataStore.class );
		CwbDataStore cwbDataStore = response.getBody();
		
		// convert cwb raw data to response format
		List<Body> bodys= DataUtils.cwbStoreToBody(cwbDataStore, pagename);
		
		apiOut.setRcode("200");
		apiOut.setRdesc("ok");
		apiOut.setPagename(pagename);
		apiOut.setReturnpage("home");
		apiOut.setBody(bodys);
		apiOut.setCanforward(true);
		return apiOut;
	}
	
	private ApiOut satelliteHandler(ApiIn apiIn, ApiOut apiOut) {
		String imgbase64 = getSatelliteBase64img();
		String imgid = "satellite";
		
		Body body = new Body();
		body.setType("img");
		body.setImgid(imgid);
		
		Imgbody imgbody = new Imgbody();
		imgbody.setImgid(imgid);
		imgbody.setImgdata(imgbase64);
		
		apiOut.setRcode("200");
		apiOut.setRdesc("ok");
		apiOut.setPagename("satellite");
		apiOut.setReturnpage("home");
		apiOut.setBody(Arrays.asList(body));
		apiOut.setImgbody(Arrays.asList(imgbody));
		apiOut.setCanforward(true);
		return apiOut;
	}
	
	private String getSatelliteBase64img() {
		// force mkdir
		String[] paths = forceCreateFolder();
		
		String picSrcFilePath = paths[0];  // ex: /Users/mayer/Documents/workspace46/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/miniweb/WEB-INF/ivrpic/cwbSatellite.jpg
		String picThumbFilePath = paths[1];  // ex: /Users/mayer/Documents/workspace46/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/miniweb/WEB-INF/ivrpicthumb/cwbSatellite.jpg
		
		// query raw satellite image
		Calendar cal = Calendar.getInstance();
    	cal.add(Calendar.HOUR, -1);
    	cal.clear(Calendar.MINUTE);
    	
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
    	String dateStr = sdf.format(cal.getTime()); // 2017-01-13-17-00 or 2017-01-12-02-00
    	
    	StringBuilder url = new StringBuilder();
    	url.append("http://www.cwb.gov.tw/V7/observe/satellite/Data/s1p/s1p-").append(dateStr).append(".jpg");
		
    	RestTemplate restTemplate = new RestTemplate();
    	restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
    	byte[] imageBytes = restTemplate.getForObject(url.toString(), byte[].class);
    	
    	// raw image resize to thumbnail and encode the thumbnail to base64 string
    	String imgBase64 = "";
    	try {
			Files.write(Paths.get(picSrcFilePath), imageBytes);
			Thumbnails.of(picSrcFilePath).size(IMG_SIZE, IMG_SIZE).toFile(picThumbFilePath);
			byte[] thumb = Files.readAllBytes(Paths.get(picThumbFilePath));
			imgBase64 = Base64.getEncoder().encodeToString(thumb);
		} catch (IOException e) {
			logger.error("Exception:", e);
		}
		
		return imgBase64;
	}
	
	private String[] forceCreateFolder() {
		String[] paths = {"", ""};
		try {
			StringBuilder sb_pictoot = new StringBuilder();
			sb_pictoot.append(File.separator).append("WEB-INF");
			String picRootPath = context.getRealPath(sb_pictoot.toString());
			
			String picSrcPath = new StringBuilder().append(picRootPath).append(File.separator).append(IMG_SRC_FOLDER_NAME).toString();
			File fileSrc = new File(picSrcPath);
			FileUtils.forceMkdir(fileSrc);
			
			String picThumbPath = new StringBuilder().append(picRootPath).append(File.separator).append(IMG_THUMB_FOLDER_NAME).toString();
			File fileThumb = new File(picThumbPath);
			FileUtils.forceMkdir(fileThumb);
			
			String picSrcFilePath = new StringBuilder().append(picSrcPath).append(File.separator).append(IMG_NAME).toString();
			String picThumbFilePath = new StringBuilder().append(picThumbPath).append(File.separator).append(IMG_NAME).toString();
			
			paths[0] = picSrcFilePath;
			paths[1] = picThumbFilePath;
			
		} catch (Exception e) {
			logger.error("Error:", e);
		}
		return paths;
	}
}
