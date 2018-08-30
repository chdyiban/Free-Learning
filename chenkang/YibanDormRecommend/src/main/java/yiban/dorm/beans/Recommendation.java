package yiban.dorm.beans;

import java.util.HashMap;
import java.util.Map;

/**
 * “推荐”对象，每个推荐中包含有对某位用户的推荐，及这么推荐的理由。<p>
 * （这个类实际上十分不符合Bean的规范...但狗急了也能跳墙，是吧hhhh
 * @author ourck
 *
 */
public class Recommendation {
	
	private static final Map<String, String> REASON_READABLE = new HashMap<String, String>();
	static {
		REASON_READABLE.put("earphone_user", "你们也许会共用一条耳机哦~");
		REASON_READABLE.put("event_lover", "外出、逛街也许是你们的共同爱好哦~");
		REASON_READABLE.put("takeout_lover", "你们也许都是热爱外卖的大吃货！");
		REASON_READABLE.put("gaming_lover", "游戏开黑怎么能少了他！");
		REASON_READABLE.put("open", "求异、创造、智能也许是你们的共同特征！");
		REASON_READABLE.put("resp", "尽职尽责、克制也许是你们的共同特征！");
		REASON_READABLE.put("extr", "热情、社交、活跃也许是你们的共同特征！");
		REASON_READABLE.put("agre", "信任、依从、利他也许是你们的共同特征！");
		REASON_READABLE.put("neur", "善于平衡情绪、温文尔雅也许是你们的共同特征！");
	}
	
	private UserInfo user;
	private String reason;
	
	// 仅调试时使用setter
//	public void setUser(UserInfo user) { this.user = user; }
//	public void setReason(String reason) { this.reason = reason; } // TODO DEBUG-ONLY
	
	public UserInfo getUserInfo() { return user; }
	public String getReason() { return reason; }
	
	public Recommendation(UserInfo user, String reason) {
		this.user = user;
		this.reason = REASON_READABLE.get(reason);
	}
	
	@Override
	public String toString() {
		StringBuilder stb = new StringBuilder();
		stb.append("为你推荐：")
		   .append(user.getUserName()).append("同学。")
		   .append(String.format("Ta的得分 - 开放性:%d分/责任心:%d分/外倾性:%d分/宜人性:%d分/情绪稳定性:%d分", 
					user.getOpenScore(),
					user.getRespScore(),
					user.getExtrScore(),
					user.getAgreScore(),
					user.getNeurScore()))
		   .append(", 是个");
		   
		   if(user.getGamingLover()) stb.append(" 游戏爱好者");
		   if(user.getEarphoneUser()) stb.append(" 耳机依赖者");
		   if(user.getEventLover()) stb.append(" 活动热衷者");
		   if(user.getTakeoutLover()) stb.append(" 外卖爱好者");
		stb.append("。<br/>").append(reason);
		return stb.toString();
	}
	
	
}
