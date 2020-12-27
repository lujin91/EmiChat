package com.emi.web.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.emi.entity.Group;
import com.emi.entity.User;
import com.emi.service.GroupService;
import com.emi.util.Constant;
import com.emi.util.JsonUtil;
import com.emi.util.UUIDUtils;

@Controller
@RequestMapping("/group")
public class GroupController {
	
	@Autowired
	private GroupService gs;
	
	@RequestMapping(value="/create", method=RequestMethod.POST)
	public void saveGrp(String groupName, HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		Map<String, Object> map = new HashMap<String, Object>();
		String info = null;
		boolean success = true;
		try {
			if(gs.existGrpName(groupName)){
				info = "群组名已存在";
				success = false;
			}
			else
			{
				User user = (User)request.getSession().getAttribute("user");
				Group grp = new Group();
				grp.setGid(UUIDUtils.getUUID());
				grp.setGroupName(groupName);
				grp.setCreateTime(new Date());
				grp.setCreaterId(user.getUid());
				gs.saveGrp(grp, user, Constant.GROUP_ROLE_OWNER);
				info = "群组创建成功";
			}
		} catch (Exception e) {
			e.printStackTrace();
			info="创建失败，请稍后再试";
			success = false;
		}
		map.put("info", info);
		map.put("success", success);
		response.setContentType("text/html; charset=UTF-8");
		response.getWriter().print(JsonUtil.map2json(map));
	}
	
}
