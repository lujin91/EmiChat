package com.emi.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	@RequestMapping("/indexUI")
	public String index(){
		return "forward:/";
	}
	
	@RequestMapping("/alertUI")
	public String alertUI(){
		return "alert";
		//return 的值 & ModelAndView中的viewName值 都会被ViewResolver执行。
		/*若不想被ViewResolver执行，则可以在前面加入redirect 或 forwad，
			redirect：后面接的内容必须是url地址，不能是项目文件路径，属于重定向。
			forwad：后面接的内容可以是url地址，也可以是项目文件路径，属于服务器跳转*/
	}
}
