package com.emi.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.emi.entity.User;
import com.emi.service.UserService;
import com.emi.util.Constant;
import com.emi.util.JsonUtil;
import com.emi.util.UUIDUtils;

@Controller
@RequestMapping("/user")
public class UserController {

	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService us;
	
	@RequestMapping("/registerUI")
	public String registerUI(){
		return "register";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public ModelAndView register(User user, HttpServletRequest request)
	{
		ModelAndView mv = new ModelAndView();
		user.setUid(UUIDUtils.getUUID());
		user.setHeadImg(request.getContextPath() + "/images/toux.png");
		user.setStatus(Constant.USER_ACTIVE);
		
		String msg = null;
		
		try 
		{
			int result = us.register(user);
			if(result > 0)
			{
				//MailUtils.sendMail(user.getEmailAddr(), "<strong style='font-size:24px;margin:5px 0;'>恭喜您成为翊米WebChat用户,请点击<a href='http://localhost:8080/EmiWebChat'>此处去登录页面吧</a></strong>");
				//msg = "您已注册成功!恭喜您成为Emi WebChat的用户,3秒后自动跳转<a href='" + request.getContextPath() + "'>登录页面</a><script type=\"text/javascript\">window.setTimeout(\"location.href='" + request.getContextPath() + "'\", 3000);</script>";
				mv.setViewName("register_success");
				return mv;
			}
			else
			{
				msg = "注册用户失败，请稍后再试！";
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
			msg = "亲，系统运行出了问题，我们会尽快修复，由此给您带来的不便我们深感抱歉！";
		}
		mv.setViewName("alert");
		mv.addObject("msg", msg);
		return mv;
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public ModelAndView login(String uname, String password, HttpServletRequest request)
	{
		ModelAndView mv = new ModelAndView();
		String msg = null;
		User user = null;
		
		try 
		{
			user = us.login(uname, password);
			if(user != null)
			{
				request.getSession().setAttribute("user", user);
				log.info("session setAttribute complete");
				mv.setViewName("redirect:/chat/main");
			}
			else
			{
				msg = "用户名或密码错误";
				mv.setViewName("forward:/indexUI");	//forward和redirect后的路径若是以'/'开头，则代表项目的web根路径（即ContextPath);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			msg = "亲，系统运行出了问题，我们会尽快修复，由此给您带来的不便我们深感抱歉！";
			mv.setViewName("alert");
		}
		mv.addObject("msg", msg);
		return mv;
	}
	
	@RequestMapping(value="/verifyUname", method=RequestMethod.POST)
	public void verifyUname(String username, HttpServletResponse response)
	{
		try {
			Map<String, String> map = new HashMap<String, String>();
			map.put("repeated", Boolean.toString(us.existUname(username)));
			response.getWriter().print(JsonUtil.map2json(map));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value="/logout")
	public String logout(HttpServletRequest request){
		request.getSession().removeAttribute("user");
		request.getSession().invalidate();
		return "redirect:/";
	}
	
}
