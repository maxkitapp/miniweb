package tw.com.maxkit.miniweb.common;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class CommonController {
	protected Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	protected ServletContext context;

}
