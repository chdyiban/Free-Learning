package yiban.dorm.web;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import ourck.solr.trigger.DIHTrigger;
import yiban.dorm.beans.Recommendation;
import yiban.dorm.beans.UserInfo;
import yiban.dorm.service.UserInfoMgr;
import yiban.dorm.service.UserRecommend;

@Controller
public class FormController {
	
	@Autowired
	private UserInfoMgr userInfoMgr;
	
	@Autowired
	private UserRecommend userRecommend;
	
	@Autowired
	private DIHTrigger trigger;
	
	@RequestMapping("/submit")
	@ResponseBody // TODO DEBUG-ONLY
	public String receive(UserInfo user) {
		userInfoMgr.calculateScores(user);
		userInfoMgr.addUser(user);
		
		List<Recommendation> list = new ArrayList<Recommendation>();
		
		// Randomize
		Random rand = new Random(47);
		List<Recommendation> recomm1 = userRecommend.recommendByScore(user);
		List<Recommendation> recomm2 = userRecommend.recommendByBehavior(user);

		int ranNum1 = recomm1.size() * rand.nextInt() / Integer.MAX_VALUE;
		int ranNum2 = recomm2.size() * rand.nextInt() / Integer.MAX_VALUE;
		
		if(recomm1.size() != 0 ) list.add(recomm1.get(ranNum1));
		if(recomm2.size() != 0 ) list.add(recomm2.get(ranNum2));
		
		trigger.invoke(true);
		
//		return list;
		
		StringBuilder stb = new StringBuilder();
		for(Recommendation recomm : list) {
			stb.append(recomm.toString()).append("<br/>").append("<br/>");
		}
		
		return stb.toString();
	}
	
}
