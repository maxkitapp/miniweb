package tw.com.maxkit.miniweb.business;

import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tw.com.maxkit.miniweb.bean.*;
import tw.com.maxkit.miniweb.common.CommonBusiness;
import tw.com.maxkit.miniweb.utils.DataUtils;

import javax.imageio.ImageIO;
import javax.net.ssl.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class WeatherBusiness extends CommonBusiness {
	private final String IMG_SRC_FOLDER_NAME = "ivrpic";
	private final String IMG_THUMB_FOLDER_NAME = "ivrpicthumb";
	private final String IMG_NAME = "cwaSatellite.jpg";
	private final int IMG_SIZE = 800;

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
//		case "listEntertainment":
//			apiOut = listEntertainmentHandler(apiIn, apiOut);
//			break;
//		case "queryEntertainment":
//			apiOut = queryEntertainmentHandler(apiIn, apiOut);
//			break;
		case "helper":
			apiOut = helperHandler(apiIn, apiOut);
			break;
		case "helperdetail":
			apiOut = helperdetailHandler(apiIn, apiOut);
			break;
		default:
			logger.debug("unknow pagename {}", pagename);
			apiOut = defaultHandler(apiIn, apiOut);
			break;
		}

		return apiOut;
	}

	private ApiOut listEntertainmentHandler(ApiIn apiIn, ApiOut apiOut) {
		try {
			apiOut = DataUtils.setEntertainmentBodys(context, apiOut);
			apiOut.setRcode("200");
			apiOut.setRdesc("ok");
		} catch (IOException e) {
			logger.error("Exception:", e);

			apiOut.setRcode("500");
			apiOut.setRdesc("IOException");
		}

		apiOut.setPagename("listEntertainment");
		apiOut.setReturnpage("home");
		apiOut.setAction("queryEntertainment");
		return apiOut;
	}

//	private ApiOut queryEntertainmentHandler(ApiIn apiIn, ApiOut apiOut) {
//		Postdata postdata = apiIn.getPostdata().get(0);
//		String postId = postdata.getId(); // eid
//		String postValue = postdata.getValue();
//
//		logger.debug("getpost, postId = {}, postValue = {}", postId, postValue);
//
//		String bodyurl = "";
//
//		switch (postValue) {
//		case "fishing":
//			bodyurl = "http://www.cwa.gov.tw/m/f/entertainment/B011.php";
//			break;
//		case "biking":
//			bodyurl = "http://www.cwa.gov.tw/m/f/entertainment/C047.php";
//			break;
//		case "stargazing":
//			bodyurl = "http://www.cwa.gov.tw/m/f/entertainment/F010.php";
//			break;
//		case "hiking":
//			bodyurl = "http://www.cwa.gov.tw/m/f/entertainment/D115.php";
//			break;
//		case "traveling":
//			bodyurl = "http://www.cwa.gov.tw/m/f/entertainment/E029.php";
//			break;
//		default:
//			break;
//		}
//
//		apiOut.setRcode("200");
//		apiOut.setRdesc("ok");
//		apiOut.setPagename(apiIn.getPagename());
//		apiOut.setBodyurl(bodyurl);
//		apiOut.setReturnpage("listEntertainment");
//
//		return apiOut;
//	}

	private ApiOut homeHandler(ApiIn apiIn, ApiOut apiOut) {
		String imgdata = "";
		try {
			imgdata = DataUtils.getPics64img(context, "weather_home");
			Body bodyImg = new Body();
			bodyImg.setType("img");
			bodyImg.setImgid("homepic");

			Body bodyDesc = new Body();
			bodyDesc.setType("span");
			bodyDesc.setValue("透過天氣小程式，可以查詢天氣資訊。");

			Imgbody imgbody = new Imgbody();
			imgbody.setImgid("homepic");
			imgbody.setImgdata(imgdata);

			apiOut.setBody(Arrays.asList(bodyImg, bodyDesc));
			apiOut.setImgbody(Arrays.asList(imgbody));

			apiOut.setRcode("200");
			apiOut.setRdesc("ok");
		} catch (Exception e) {
			logger.error("Exception:", e);
			apiOut.setRcode("500");
			apiOut.setRdesc("Exception");
		}

		apiOut.setPagename("home");

		return apiOut;
	}

	private ApiOut defaultHandler(ApiIn apiIn, ApiOut apiOut) {
		apiOut.setRcode("404");
		apiOut.setRdesc("Pagename " + apiIn.getPagename() + " not found");
		return apiOut;
	}

	private ApiOut getWeatherHandler(ApiIn apiIn, ApiOut apiOut, String pagename) {
		// query cwa raw data
		List<Body> bodys = new ArrayList<>();
		try {
//			RestTemplate restTemplate = getRestTemplate();

//			HttpHeaders headers = new HttpHeaders();
//			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//			MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//			map.add("area", pagename);
//
//			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
//
//			ResponseEntity<CwbDataStore> response = restTemplate.postForEntity(API_GETWEATHER_URL, request,
//					CwbDataStore.class);
//			CwbDataStore cwaDataStore = response.getBody();

//			bodys = DataUtils.cwaStoreToBody(cwaDataStore, pagename);

			String API_GETWEATHER_URL = "https://www.cwa.gov.tw/Data/js/fcst/W50_Data.js";

			String ccode = "66";
			if( pagename.equals("taichung") ) {
				ccode="66";
			} else if( pagename.equals("taipei")) {
				ccode="63";
			} else if( pagename.equals("kaohsiung")) {
				ccode="64";
			}

			HttpGet request1 = new HttpGet(API_GETWEATHER_URL);
			HttpResponse response1 = getHttpClient().execute(request1);
			int code = response1.getStatusLine().getStatusCode();

			String output1 = "";
			try (BufferedReader br = new BufferedReader(new InputStreamReader((response1.getEntity().getContent())));) {
				// Read in all of the post results into a String.

				Boolean keepGoing = true;
				while (keepGoing) {
					String currentLine = br.readLine();

					if (currentLine == null) {
						keepGoing = false;
					} else {
						output1 += currentLine;
					}
				}

//				logger.debug("Response-->" + output1);
			} catch (Exception e) {
				logger.error("Exception", e);
			}

			Body body0 = new Body();
			body0.setType("span");
			body0.setValue("天氣預報");
			body0.setSize(18);
			bodys.add(body0);

			String ccodedesc = "'"+ccode+"':{";

			String output = output1.substring(output1.indexOf(ccodedesc)+7 , output1.indexOf("}", output1.indexOf(ccodedesc)) );

			if( !output.equals("")) {
//				String output = "<div class='box-content'><div class='responsive-table'>多雲，溫度逐漸回升，早晚仍涼，請適時調整衣物以免著涼。<BR><BR>昨天（２５日）冷空氣強度仍有東北季風的程度，整天濕涼，臺北站測得高溫19.9度，清晨低溫15.8度；降雨方面，部份地區有局部短暫降雨，但中午過後皆逐漸減緩。<BR><BR>今天(２６日）東北季風減弱，天氣多雲，整體溫度逐漸回升，但早晚仍涼，山區早上有局部短暫雨；白天氣溫15至19度，請適時調整衣物以免著涼。<BR><BR></div></div>";

				String[] B = output.split("\\r?\\n");
				B[0]=B[0].replace("<div class='box-content'><div class='responsive-table'>", "");
				B[B.length-1]=B[B.length-1].replace("</div></div>", "");

				for (int i = 0; i < B.length; i++) {
					if( !B[i].equals("")) {
						Body bodyDesc = new Body();
						bodyDesc.setType("span");
						bodyDesc.setValue(B[i]);
						bodyDesc.setSize(18);
						bodys.add(bodyDesc);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		apiOut.setRcode("200");
		apiOut.setRdesc("ok");
		apiOut.setPagename(pagename);
		apiOut.setReturnpage("home");
		apiOut.setBody(bodys);
		apiOut.setCanforward(true);
		return apiOut;
	}

	private ApiOut helperHandler(ApiIn apiIn, ApiOut apiOut) {
		List<Body> bodys = DataUtils.generateHelperBodys();

		apiOut.setRcode("200");
		apiOut.setRdesc("ok");
		apiOut.setPagename("helper");
		apiOut.setReturnpage("home");
		apiOut.setAction("helperdetail");
		apiOut.setBody(bodys);
		return apiOut;
	}

	private ApiOut helperdetailHandler(ApiIn apiIn, ApiOut apiOut) {
		Postdata postdata = apiIn.getPostdata().get(0);
		String postId = postdata.getId(); // item
		String postValue = postdata.getValue();

		logger.debug("getpost, postId = {}, postValue = {}", postId, postValue);

		apiOut.setRcode("200");
		apiOut.setRdesc("ok");
		apiOut.setPagename(apiIn.getPagename());
		apiOut.setReturnpage("helper");

		switch (postValue) {
		case "text":
			apiOut = DataUtils.setTextBodys(apiOut);
			break;
		case "textarea":
			apiOut = DataUtils.setTextareaBodys(apiOut);
			break;
		case "option":
			try {
				apiOut = DataUtils.setOptionBodys(context, apiOut);
			} catch (Exception e) {
				logger.error("Error:", e);

				apiOut.setRcode("500");
				apiOut.setRdesc("Exception");
			}

			break;
		case "checkbox":
			try {
				apiOut = DataUtils.setCheckboxBodys(context, apiOut);
			} catch (Exception e) {
				logger.error("Error:", e);

				apiOut.setRcode("500");
				apiOut.setRdesc("Exception");
			}

			break;
		case "span":
			apiOut = DataUtils.setSpanBodys(apiOut);
			break;
		case "img":
			try {
				apiOut = DataUtils.setImgBodys(context, apiOut);
			} catch (IOException e) {
				logger.error("Error:", e);
			}
			break;
		default:
			break;
		}

		return apiOut;
	}

	private ApiOut satelliteHandler(ApiIn apiIn, ApiOut apiOut) {
		String imgbase64 = getSatelliteBase64img();
		String imgid = "satellite";

		Body bodyDesc = new Body();
		bodyDesc.setType("span");
		bodyDesc.setValue("台灣衛星雲圖：");

		Body bodyImg = new Body();
		bodyImg.setType("img");
		bodyImg.setImgid(imgid);

		Imgbody imgbody = new Imgbody();
		imgbody.setImgid(imgid);
		imgbody.setImgdata(imgbase64);

		apiOut.setRcode("200");
		apiOut.setRdesc("ok");
		apiOut.setPagename("satellite");
		apiOut.setReturnpage("home");
		apiOut.setBody(Arrays.asList(bodyDesc, bodyImg));
		apiOut.setImgbody(Arrays.asList(imgbody));
		apiOut.setCanforward(true);
		return apiOut;
	}

	private String getSatelliteBase64img() {
		// force mkdir
		String[] paths = forceCreateFolder();

		String picSrcFilePath = paths[0]; // ex:
											// /Users/mayer/Documents/workspace46/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/miniweb/WEB-INF/ivrpic/cwaSatellite.jpg
		String picThumbFilePath = paths[1]; // ex:
											// /Users/mayer/Documents/workspace46/.metadata/.plugins/org.eclipse.wst.server.core/tmp0/wtpwebapps/miniweb/WEB-INF/ivrpicthumb/cwaSatellite.jpg

		// query raw satellite image
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, -1);
		cal.clear(Calendar.MINUTE);

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm");
		String dateStr = sdf.format(cal.getTime()); // 2017-01-13-17-00 or
													// 2017-01-12-02-00

		StringBuilder url = new StringBuilder();
		url.append("https://www.cwa.gov.tw/Data/satellite/TWI_IR1_MB_800/TWI_IR1_MB_800-").append(dateStr).append(".jpg");

		logger.debug("get satellite image url = {}", url);
		byte[] imageBytes = new byte[0];
//		try {
//			logger.debug("get satellite RestTemplate");
//			RestTemplate restTemplate = getRestTemplate();
//			restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
//			imageBytes = restTemplate.getForObject(url.toString(), byte[].class);
//			logger.debug("get satellite RestTemplate length={}", imageBytes.length);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}

//		try {
//			logger.debug("get satellite BufferedImage");
//			HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier(){
//				public boolean verify(String arg0, SSLSession arg1) {
//					return true;
//				}});
//
//			BufferedImage image = ImageIO.read(new URL(url.toString()));
////			int height = image.getHeight();
////			int width = image.getWidth();
//			imageBytes = toByteArray(image, "jpg");
//			logger.debug("get satellite BufferedImage length={}", imageBytes.length);
//		} catch (IOException e) {}
//		// raw image resize to thumbnail and encode the thumbnail to base64
//		// string
//		String imgBase64 = "";
//		if( imageBytes.length>0 ) {
//			try {
//				Files.write(Paths.get(picSrcFilePath), imageBytes);
//				Thumbnails.of(picSrcFilePath).size(IMG_SIZE, IMG_SIZE).toFile(picThumbFilePath);
//				byte[] thumb = Files.readAllBytes(Paths.get(picThumbFilePath));
//				imgBase64 = Base64.getEncoder().encodeToString(thumb);
//			} catch (IOException e) {
//				logger.error("Exception:", e);
//			}
//		}
		try {
			logger.debug("get satellite BufferedImage");
			downloadFileFromURL(url.toString(), picSrcFilePath );
		} catch (IOException e) {}

		String imgBase64 = "";
		try {
			Thumbnails.of(picSrcFilePath).size(IMG_SIZE, IMG_SIZE).toFile(picThumbFilePath);
			byte[] thumb = Files.readAllBytes(Paths.get(picThumbFilePath));
			imgBase64 = Base64.getEncoder().encodeToString(thumb);
		} catch (IOException e) {
			logger.error("Exception:", e);
		}

		return imgBase64;
	}
	private void downloadFileFromURL(String search, String path) throws IOException {
		// This will get input data from the server
		InputStream inputStream = null;
		// This will read the data from the server;
		OutputStream outputStream = null;
		try {
			// This will open a socket from client to server
			URL url = new URL(search);

			// This user agent is for if the server wants real humans to visit
			String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36";

			// This socket type will allow to set user_agent
			URLConnection con = url.openConnection();

			// Setting the user agent
			con.setRequestProperty("User-Agent", USER_AGENT);

			//Getting content Length
			int contentLength = con.getContentLength();
			logger.debug("File contentLength = " + contentLength + " bytes");

			// Requesting input data from server
			inputStream = con.getInputStream();

//            logger.debug("FileOutputStream path={}", path);
			// Open local file writer
			outputStream = new FileOutputStream(path);

			// Limiting byte written to file per loop
			byte[] buffer = new byte[10000];

			// Increments file size
			int length;
			int downloaded = 0;
//            logger.debug("downloaded={}", downloaded);

			// Looping until server finishes
			while ((length = inputStream.read(buffer)) != -1)
			{
				// Writing data
				outputStream.write(buffer, 0, length);
				downloaded+=length;
//                logger.debug("Downlad Status: " + (downloaded * 100) / (contentLength * 1.0) + "%");
			}
		} catch (Exception ex) {
			//Logger.getLogger(WebCrawler.class.getName()).log(Level.SEVERE, null, ex);
			ex.printStackTrace();
		} finally {
			if( inputStream!=null ) {
				inputStream.close();
			}
			if( outputStream!=null) {
				outputStream.close();
			}
		}

	}

	private static byte[] toByteArray(BufferedImage bi, String format)
			throws IOException {

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bi, format, baos);
		baos.flush();
		byte[] bytes = baos.toByteArray();
		return bytes;

	}

	public CloseableHttpClient getHttpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
				.loadTrustMaterial(null, acceptingTrustStrategy)
				.build();

		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLSocketFactory(csf)
				.build();
		return httpClient;
	}


	public RestTemplate getRestTemplate() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
		TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

		SSLContext sslContext = org.apache.http.ssl.SSLContexts.custom()
				.loadTrustMaterial(null, acceptingTrustStrategy)
				.build();

		SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

		CloseableHttpClient httpClient = HttpClients.custom()
				.setSSLSocketFactory(csf)
				.build();

		HttpComponentsClientHttpRequestFactory requestFactory =
				new HttpComponentsClientHttpRequestFactory();

		requestFactory.setHttpClient(httpClient);
		RestTemplate restTemplate = new RestTemplate(requestFactory);
//		restTemplate.getMessageConverters().add(new WxMappingJackson2HttpMessageConverter());
//		restTemplate.getMessageConverters().add(new MyGsonHttpMessageConverter());
		return restTemplate;
	}

	private String[] forceCreateFolder() {
		String[] paths = { "", "" };
		try {
			StringBuilder sb_picroot = new StringBuilder();
			sb_picroot.append(File.separator).append("WEB-INF");
			String picRootPath = context.getRealPath(sb_picroot.toString());

			String picSrcPath = new StringBuilder().append(picRootPath).append(File.separator)
					.append(IMG_SRC_FOLDER_NAME).toString();
			File fileSrc = new File(picSrcPath);
			FileUtils.forceMkdir(fileSrc);

			String picThumbPath = new StringBuilder().append(picRootPath).append(File.separator)
					.append(IMG_THUMB_FOLDER_NAME).toString();
			File fileThumb = new File(picThumbPath);
			FileUtils.forceMkdir(fileThumb);

			String picSrcFilePath = new StringBuilder().append(picSrcPath).append(File.separator).append(IMG_NAME)
					.toString();
			String picThumbFilePath = new StringBuilder().append(picThumbPath).append(File.separator).append(IMG_NAME)
					.toString();

			paths[0] = picSrcFilePath;
			paths[1] = picThumbFilePath;

		} catch (Exception e) {
			logger.error("Error:", e);
		}
		return paths;
	}

//	public class WxMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
//		public WxMappingJackson2HttpMessageConverter() {
//			List<MediaType> mediaTypes = new ArrayList<>();
//			mediaTypes.add(MediaType.TEXT_PLAIN);
//			mediaTypes.add(MediaType.TEXT_HTML);  //加入text/html类型的支持
//			setSupportedMediaTypes(mediaTypes);// tag6
//		}
//	}
//
//	public class MyGsonHttpMessageConverter extends GsonHttpMessageConverter {
//		public MyGsonHttpMessageConverter() {
//			List<MediaType> types = Arrays.asList(
//					new MediaType("text", "html", DEFAULT_CHARSET),
//					new MediaType("application", "json", DEFAULT_CHARSET),
//					new MediaType("application", "*+json", DEFAULT_CHARSET)
//			);
//			super.setSupportedMediaTypes(types);
//		}
//	}

}
