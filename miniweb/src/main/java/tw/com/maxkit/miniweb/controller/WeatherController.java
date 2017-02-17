package tw.com.maxkit.miniweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tw.com.maxkit.miniweb.bean.ApiIn;
import tw.com.maxkit.miniweb.bean.ApiOut;
import tw.com.maxkit.miniweb.business.WeatherBusiness;
import tw.com.maxkit.miniweb.common.CommonController;

@Controller
@RequestMapping("/weather")
public class WeatherController extends CommonController {
	@Autowired
	private WeatherBusiness weatherBiz;

	@ResponseBody
	@PostMapping(path = "/app", consumes = "application/json")
	public ApiOut app(@RequestBody ApiIn apiIn) {
		ApiOut apiOut = weatherBiz.requestHandler(apiIn);
		return apiOut;
	}
}
