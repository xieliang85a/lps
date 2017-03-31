package cn.com.xl.lps.app;

import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/app")
public class APPController {
	protected Logger LOGGER = LogManager.getLogger(this.getClass());
	
	@RequestMapping("/test")
	public void test(HttpServletResponse response) {
		try {
			String curDate = new Date().toLocaleString();
			LOGGER.debug("APP client invoke :" + curDate);
			response.getWriter().println("server return :"+curDate);
		} catch (IOException e) {
			e.printStackTrace();
		}
	} 
}
