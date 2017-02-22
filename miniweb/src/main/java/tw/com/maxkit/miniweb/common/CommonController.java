package tw.com.maxkit.miniweb.common;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import tw.com.maxkit.miniweb.bean.ApiIn;

public class CommonController {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected ServletContext context;
	
	protected boolean isValid(ApiIn apiIn, String auth) {
		if(apiIn == null) {
			return false;
		}
		
		String exectoken = apiIn.getExectoken();
		if(StringUtils.isEmpty(exectoken)) {
			return false;
		}
		
		if(!exectoken.equals(auth)) {
			return false;
		}
		
		return true;
	}

}
