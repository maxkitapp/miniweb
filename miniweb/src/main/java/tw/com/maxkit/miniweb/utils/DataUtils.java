package tw.com.maxkit.miniweb.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.util.StringUtils;

import tw.com.maxkit.miniweb.bean.ApiOut;
import tw.com.maxkit.miniweb.bean.Body;
import tw.com.maxkit.miniweb.bean.Imgbody;
import tw.com.maxkit.miniweb.bean.Option;
import tw.com.maxkit.miniweb.bean.crm.Contact;
import tw.com.maxkit.miniweb.bean.cwb.CwbDataStore;
import tw.com.maxkit.miniweb.bean.cwb.Location;
import tw.com.maxkit.miniweb.bean.cwb.Locations;
import tw.com.maxkit.miniweb.bean.cwb.Time;
import tw.com.maxkit.miniweb.bean.cwb.WeatherElement;

public class DataUtils {
	public static synchronized List<Body> cwbStoreToBody(CwbDataStore cwbStore, String pagename) {
		List<Body> bodys = new ArrayList<>();

		List<Locations> locations = cwbStore.getRecords().getLocations();
		List<Location> location = locations.get(0).getLocation();
		List<WeatherElement> weatherElement = location.get(0).getWeatherElement();
		List<Time> times = weatherElement.get(0).getTime();

		String title = locations.get(0).getLocationsName();
		Body body0 = new Body();
		body0.setType("span");
		body0.setValue(title + "天氣預報");
		body0.setSize(18);

		bodys.add(body0);
		bodys.add(generateSpanHr());

		for (int i = 0; i < 10; i++) {
			Time time = times.get(i);
			String starttime = time.getStartTime(); // "2017-01-09 18:00:00"
			String endtime = time.getEndTime(); // "2017-01-09 21:00:00"
			String desc = time.getElementValue(); // "晴天。 降雨機率 0%。 溫度攝氏19度。
													// 稍有寒意。 偏北風
													// 平均風速3至4級(每秒6公尺)。
													// 相對濕度為68%。"
			Body bodyTime = new Body();
			bodyTime.setType("span");
			bodyTime.setValue(starttime + " ~ " + endtime);

			Body bodyDesc = new Body();
			bodyDesc.setType("span");
			bodyDesc.setValue(desc);

			Collections.addAll(bodys, bodyTime, bodyDesc, generateSpanHr());
		}

		return bodys;
	}

	public static synchronized List<Body> contactsToBody(String keyword, List<Contact> contacts) {
		List<Body> bodys = new ArrayList<>();
		Body body = new Body();
		body.setId("cid");
		body.setValue("搜尋關鍵字：" + keyword + "；搜尋結果：" + contacts.size() + " 筆");
		body.setType("option");
		body.setSize(18);

		List<Option> options = new ArrayList<>();
		for (Contact contact : contacts) {
			StringBuilder sb = new StringBuilder();
			sb.append(generateDisplayname(contact.getFirstName(), contact.getLastName())).append(" (")
					.append(contact.getAccname()).append(")");

			Option option = new Option();
			option.setOptname(sb.toString());
			option.setOptvalue(contact.getId());
			options.add(option);
		}

		body.setOptionlist(options);
		bodys.add(body);
		return bodys;
	}

	public static synchronized List<Body> contactToBody(Contact contact) {
		List<Body> bodys = new ArrayList<>();

		String lastname = contact.getLastName();
		String firstname = contact.getFirstName();
		String displayname = generateDisplayname(firstname, lastname);
		Body bodyName = generateSpanBody("姓名", displayname);
		bodys.add(bodyName);

		String accname = contact.getAccname();
		Body bodyAccname = generateSpanBody("公司", accname);
		bodys.add(bodyAccname);

		String title = contact.getTitle();
		if (!StringUtils.isEmpty(title)) {
			Body bodyTitle = generateSpanBody("職稱", title);
			bodys.add(bodyTitle);
		}

		String department = contact.getDepartment();
		if (!StringUtils.isEmpty(department)) {
			Body bodyDepartment = generateSpanBody("部門", department);
			bodys.add(bodyDepartment);
		}

		String description = contact.getDescription();
		if (!StringUtils.isEmpty(description)) {
			Body bodyDescription = generateSpanBody("描述", description);
			bodys.add(bodyDescription);
		}

		String phonemobile = contact.getPhoneMobile();
		if (!StringUtils.isEmpty(phonemobile)) {
			Body bodyMobile = generateSpanBody("手機", phonemobile);
			bodys.add(bodyMobile);
		}

		String phonework = contact.getPhoneWork();
		if (!StringUtils.isEmpty(phonework)) {
			Body bodyPhonework = generateSpanBody("公司電話", phonework);
			bodys.add(bodyPhonework);
		}

		String phoneother = contact.getPhoneOther();
		if (!StringUtils.isEmpty(phoneother)) {
			Body bodyPhoneother = generateSpanBody("其他電話", phoneother);
			bodys.add(bodyPhoneother);
		}

		String address = contact.getPrimaryAddressStreet();
		if (!StringUtils.isEmpty(address)) {
			Body bodyAddress = generateSpanBody("地址", address);
			bodys.add(bodyAddress);
		}

		return bodys;
	}

	public static ApiOut setEntertainmentBodys(ServletContext context, ApiOut apiOut) throws IOException {
		List<Body> bodys = new ArrayList<>();
		List<Imgbody> imgbodys = new ArrayList<>();

		Body body = new Body();
		body.setId("eid");
		body.setValue("請選擇育樂氣象：");
		body.setType("option");
		body.setSize(18);

		List<Option> options = new ArrayList<>();

		Option optionFishing = new Option();
		optionFishing.setOptname("海釣");
		optionFishing.setOptvalue("fishing");
		optionFishing.setOptimgid("fishing");
		Imgbody imgFishing = new Imgbody();
		imgFishing.setImgid("fishing");
		imgFishing.setImgdata(getPics64img(context, "weather_btn_icon1"));

		Option optionBiking = new Option();
		optionBiking.setOptname("單車");
		optionBiking.setOptvalue("biking");
		optionBiking.setOptimgid("biking");
		Imgbody imgBiking = new Imgbody();
		imgBiking.setImgid("biking");
		imgBiking.setImgdata(getPics64img(context, "weather_btn_icon2"));

		Option optionStargazing = new Option();
		optionStargazing.setOptname("觀星");
		optionStargazing.setOptvalue("stargazing");
		optionStargazing.setOptimgid("stargazing");
		Imgbody imgStargazing = new Imgbody();
		imgStargazing.setImgid("stargazing");
		imgStargazing.setImgdata(getPics64img(context, "weather_btn_icon3"));

		Option optionHiking = new Option();
		optionHiking.setOptname("登山");
		optionHiking.setOptvalue("hiking");
		optionHiking.setOptimgid("hiking");
		Imgbody imgHiking = new Imgbody();
		imgHiking.setImgid("hiking");
		imgHiking.setImgdata(getPics64img(context, "weather_btn_icon4"));

		Option optionTraveling = new Option();
		optionTraveling.setOptname("旅遊");
		optionTraveling.setOptvalue("traveling");
		optionTraveling.setOptimgid("traveling");
		Imgbody imgTraveling = new Imgbody();
		imgTraveling.setImgid("traveling");
		imgTraveling.setImgdata(getPics64img(context, "weather_btn_icon5"));

		Collections.addAll(options, optionFishing, optionBiking, optionStargazing, optionHiking, optionTraveling);

		body.setOptionlist(options);
		bodys.add(body);

		apiOut.setBody(bodys);

		Collections.addAll(imgbodys, imgFishing, imgBiking, imgStargazing, imgHiking, imgTraveling);
		apiOut.setImgbody(imgbodys);
		return apiOut;
	}

	public static List<Body> generateHelperBodys() {
		List<Body> bodys = new ArrayList<>();
		Body body = new Body();
		body.setId("item");
		body.setValue("請選擇項目：");
		body.setType("option");
		body.setSize(18);

		List<Option> options = new ArrayList<>();

		Option optionText = new Option();
		optionText.setOptname("text");
		optionText.setOptvalue("text");

		Option optionTextarea = new Option();
		optionTextarea.setOptname("textarea");
		optionTextarea.setOptvalue("textarea");

		Option option = new Option();
		option.setOptname("option");
		option.setOptvalue("option");

		Option optionCheckbox = new Option();
		optionCheckbox.setOptname("checkbox");
		optionCheckbox.setOptvalue("checkbox");

		Option optionSpan = new Option();
		optionSpan.setOptname("span");
		optionSpan.setOptvalue("span");

		Option optionImg = new Option();
		optionImg.setOptname("img");
		optionImg.setOptvalue("img");

		Collections.addAll(options, optionText, optionTextarea, option, optionCheckbox, optionSpan, optionImg);

		body.setOptionlist(options);
		bodys.add(body);
		return bodys;
	}

	public static ApiOut setTextBodys(ApiOut apiOut) {
		List<Body> bodys = new ArrayList<>();
		Body any = new Body();
		any.setType("text");
		any.setValue("Text, Keyboard = any");
		any.setKeyboard("any");

		Body en = new Body();
		en.setType("text");
		en.setValue("Text, Keyboard = en");
		en.setKeyboard("en");

		Body digit = new Body();
		digit.setType("text");
		digit.setValue("Text, Keyboard = digit");
		digit.setKeyboard("digit");

		Body en_digit = new Body();
		en_digit.setType("text");
		en_digit.setValue("Text, Keyboard = en_digit");
		en_digit.setKeyboard("en_digit");

		Body email = new Body();
		email.setType("text");
		email.setValue("Text, Keyboard = email");
		email.setKeyboard("email");

		Body date = new Body();
		date.setType("text");
		date.setValue("Text, Keyboard = date");
		date.setKeyboard("date");

		Body time = new Body();
		time.setType("text");
		time.setValue("Text, Keyboard = time");
		time.setKeyboard("time");

		Body datetime = new Body();
		datetime.setType("text");
		datetime.setValue("Text, Keyboard = datetime");
		datetime.setKeyboard("datetime");

		Collections.addAll(bodys, any, en, digit, en_digit, email, date, time, datetime);
		apiOut.setBody(bodys);
		return apiOut;
	}

	public static ApiOut setTextareaBodys(ApiOut apiOut) {
		List<Body> bodys = new ArrayList<>();
		Body bodyTextarea = new Body();
		bodyTextarea.setType("textarea");
		bodyTextarea.setValue("Textarea, Keyboard = any");
		bodyTextarea.setKeyboard("any");

		Collections.addAll(bodys, bodyTextarea);
		apiOut.setBody(bodys);
		return apiOut;
	}

	public static ApiOut setOptionBodys(ServletContext context, ApiOut apiOut) throws Exception {
		List<Body> bodys = new ArrayList<>();

		Body body1 = new Body();
		body1.setId("optionid1");
		body1.setValue("請選擇選項：");
		body1.setType("option");
		body1.setSize(18);

		List<Option> options = new ArrayList<>();
		Option option1 = new Option();
		option1.setOptname("選項一");
		option1.setOptvalue("o1");
		Option option2 = new Option();
		option2.setOptname("選項二");
		option2.setOptvalue("o2");
		Collections.addAll(options, option1, option2);
		body1.setOptionlist(options);

		Body body2 = new Body();
		body2.setId("optionid2");
		body2.setValue("請選擇選項(有圖片)：");
		body2.setType("option");
		body2.setSize(18);
		List<Option> options1 = new ArrayList<>();
		Option option3 = new Option();
		option3.setOptname("選項三");
		option3.setOptvalue("o3");
		option3.setOptimgid("o3");
		Option option4 = new Option();
		option4.setOptname("選項四");
		option4.setOptvalue("o4");
		option4.setOptimgid("o4");
		Collections.addAll(options1, option3, option4);
		body2.setOptionlist(options1);

		Imgbody imgbody1 = new Imgbody();
		imgbody1.setImgid("o3");
		imgbody1.setImgdata(getPics64img(context, "option_icon1"));

		Imgbody imgbody2 = new Imgbody();
		imgbody2.setImgid("o4");
		imgbody2.setImgdata(getPics64img(context, "option_icon2"));

		apiOut.setImgbody(Arrays.asList(imgbody1, imgbody2));

		Collections.addAll(bodys, body1, body2);

		apiOut.setBody(bodys);
		return apiOut;
	}

	public static ApiOut setCheckboxBodys(ServletContext context, ApiOut apiOut) throws Exception {
		List<Body> bodys = new ArrayList<>();

		Body body1 = new Body();
		body1.setId("checkid");
		body1.setValue("請勾選選項：");
		body1.setType("checkbox");
		body1.setSize(18);
		List<Option> options1 = new ArrayList<>();
		Option option1 = new Option();
		option1.setOptname("選項一");
		option1.setOptvalue("c1");
		Option option2 = new Option();
		option2.setOptname("選項二");
		option2.setOptvalue("c2");
		Collections.addAll(options1, option1, option2);
		body1.setOptionlist(options1);

		Body body2 = new Body();
		body2.setId("checkid");
		body2.setValue("請勾選選項(有圖片)：");
		body2.setType("checkbox");
		body2.setSize(18);
		List<Option> options2 = new ArrayList<>();
		Option option3 = new Option();
		option3.setOptname("選項三");
		option3.setOptvalue("c3");
		option3.setOptimgid("c3");
		Option option4 = new Option();
		option4.setOptname("選項四");
		option4.setOptvalue("c4");
		option4.setOptimgid("c4");
		Collections.addAll(options2, option3, option4);
		body2.setOptionlist(options2);

		Imgbody imgbody1 = new Imgbody();
		imgbody1.setImgid("c3");
		imgbody1.setImgdata(getPics64img(context, "option_icon1"));

		Imgbody imgbody2 = new Imgbody();
		imgbody2.setImgid("c4");
		imgbody2.setImgdata(getPics64img(context, "option_icon2"));

		apiOut.setImgbody(Arrays.asList(imgbody1, imgbody2));

		Collections.addAll(bodys, body1, body2);
		apiOut.setBody(bodys);
		return apiOut;
	}

	public static ApiOut setSpanBodys(ApiOut apiOut) {
		List<Body> bodys = new ArrayList<>();

		Body body1 = new Body();
		body1.setType("span");
		body1.setValue("span");

		Body body2 = new Body();
		body2.setType("span");
		body2.setColor("#D1690E");
		body2.setSize(24);
		body2.setBgcolor("#E6E6FA");

		Collections.addAll(bodys, body1, body2);
		apiOut.setBody(bodys);
		return apiOut;
	}

	public static ApiOut setImgBodys(ServletContext context, ApiOut apiOut) throws IOException {
		String imgdata = DataUtils.getPics64img(context, "crm");

		Body bodyImg = new Body();
		bodyImg.setType("img");
		bodyImg.setImgid("homepic");

		Imgbody imgbody = new Imgbody();
		imgbody.setImgid("homepic");
		imgbody.setImgdata(imgdata);

		apiOut.setBody(Arrays.asList(bodyImg));
		apiOut.setImgbody(Arrays.asList(imgbody));
		return apiOut;
	}

	// appname = crm or weather
	public static String getPics64img(ServletContext context, String picname) throws IOException {
		StringBuilder sb_pic = new StringBuilder();
		sb_pic.append(File.separator).append("WEB-INF").append(File.separator).append("pics").append(File.separator)
				.append(picname).append(".png");
		String picPath = context.getRealPath(sb_pic.toString());

		String imgBase64 = "";
		try {
			byte[] bytes = Files.readAllBytes(Paths.get(picPath));
			imgBase64 = Base64.getEncoder().encodeToString(bytes);
		} catch (IOException e) {
			throw e;
		}

		return imgBase64;
	}

	private static Body generateSpanHr() {
		Body body = new Body();
		body.setType("span");
		body.setValue("--------");
		return body;

	}

	private static Body generateSpanBody(String columnName, String value) {
		Body body = new Body();
		body.setType("span");
		body.setValue(columnName + "：" + value);
		return body;
	}

	private static String generateDisplayname(String firstname, String lastname) {
		StringBuilder sb = new StringBuilder();
		if (!StringUtils.isEmpty(firstname)) {
			sb.append(firstname).append(" ");
		}

		if (!StringUtils.isEmpty(lastname)) {
			sb.append(lastname);
		}

		return sb.toString();
	}
}
