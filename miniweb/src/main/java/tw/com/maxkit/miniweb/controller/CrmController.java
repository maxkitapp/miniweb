package tw.com.maxkit.miniweb.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import tw.com.maxkit.miniweb.bean.ApiIn;
import tw.com.maxkit.miniweb.bean.ApiOut;
import tw.com.maxkit.miniweb.business.CrmBusiness;
import tw.com.maxkit.miniweb.common.CommonController;

@Controller
@RequestMapping("/crm")
public class CrmController extends CommonController {
	@Autowired
	private CrmBusiness crmBiz;
	@Value("${auth.crm}") 
	private String auth;
	
	@ResponseBody
	@PostMapping(path = "/app", consumes = "application/json")
	public ApiOut app(@RequestBody ApiIn apiIn) {
		ApiOut apiOut = null;
		
		if(isValid(apiIn, auth)) {
			apiOut = crmBiz.requestHandler(apiIn);	
		} else {
			apiOut = new ApiOut();
			apiOut.setRcode("403");
			apiOut.setRdesc("Forbidden");
		}
		
		return apiOut;
	}

}
