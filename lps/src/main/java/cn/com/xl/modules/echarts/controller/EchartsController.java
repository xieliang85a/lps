package cn.com.xl.modules.echarts.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.com.xl.common.base.BaseController;

@Controller
@RequestMapping("/echarts")
public class EchartsController extends BaseController {

	@GetMapping
	public String echarts() {
		return "/modules/echarts/echarts.html";
	}
	
}
