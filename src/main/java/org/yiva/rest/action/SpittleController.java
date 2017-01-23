package org.yiva.rest.action;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.yiva.rest.pojo.UserInfo;
import org.yiva.rest.service.SpittlerService;

@Controller
@RequestMapping("/userinfo")
public class SpittleController {

	private SpittlerService spittlerService;

	@Autowired
	public SpittleController(SpittlerService spittlerService) {
		this.spittlerService = spittlerService;
	}

	@RequestMapping(value = "/{id}/{name}", method = RequestMethod.GET, headers = { "Accept=application/json" })
	public @ResponseBody ArrayList<UserInfo> getSpittle(
			@PathVariable("id") int id, @PathVariable("name") String name,
			Model model) {
		System.out.println("Get: " + id + ", " + name);
		UserInfo user = new UserInfo();
		user.setId(id);
		user.setName(name);
		ArrayList<UserInfo> list = new ArrayList<UserInfo>();
		list.add(user);
		return list;
	}

	@RequestMapping(value = "/{id}/{user}", method = RequestMethod.PUT)
	public String putSpittle(@PathVariable("id") long id,
			@PathVariable("user") String user, Model model) {
		model.addAttribute(id);
		model.addAttribute(user);
		System.out.println("Put: " + id + ", " + user);
		return "spittles/view";
	}

}
