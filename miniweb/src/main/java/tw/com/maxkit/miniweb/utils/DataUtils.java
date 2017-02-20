package tw.com.maxkit.miniweb.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import javax.servlet.ServletContext;

import org.springframework.util.StringUtils;

import tw.com.maxkit.miniweb.bean.Body;
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

	public static List<Body> generateEntertainmentBodys() {
		List<Body> bodys = new ArrayList<>();
		Body body = new Body();
		body.setId("eid");
		body.setValue("請選擇育樂氣象：");
		body.setType("option");
		body.setSize(18);

		List<Option> options = new ArrayList<>();

		Option optionFishing = new Option();
		optionFishing.setOptname("海釣");
		optionFishing.setOptvalue("fishing");

		Option optionBiking = new Option();
		optionBiking.setOptname("單車");
		optionBiking.setOptvalue("biking");

		Option optionStargazing = new Option();
		optionStargazing.setOptname("觀星");
		optionStargazing.setOptvalue("stargazing");

		Option optionHiking = new Option();
		optionHiking.setOptname("登山");
		optionHiking.setOptvalue("hiking");

		Option optionTraveling = new Option();
		optionTraveling.setOptname("旅遊");
		optionTraveling.setOptvalue("traveling");

		Collections.addAll(options, optionFishing, optionBiking, optionStargazing, optionHiking, optionTraveling);

		body.setOptionlist(options);
		bodys.add(body);
		return bodys;
	}

	public static List<Body> generateHelperBodys() {
		List<Body> bodys = new ArrayList<>();
		Body body = new Body();
		body.setId("item");
		body.setValue("請項目：");
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

	// appname = crm or weather
	public static String getHomepic64img(ServletContext context, String appname) throws IOException{
		StringBuilder sb_pic = new StringBuilder();
		sb_pic.append(File.separator).append("WEB-INF").append(File.separator).append("homepic").append(File.separator)
				.append(appname).append(".png");
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
