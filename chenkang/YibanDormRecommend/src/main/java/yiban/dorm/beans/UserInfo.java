package yiban.dorm.beans;

public class UserInfo {
	
	private String userName;
	private boolean gaming_lover;
	private boolean earphone_user;
	private boolean event_lover;
	private boolean takeout_lover;
	private String[] tags;
	private int openScore;
	private int respScore;
	private int extrScore;
	private int agreScore;
	private int neurScore;
	
	public void setUserName(String userName) { this.userName = userName; }
	public void setGamingLover(boolean flag) { this.gaming_lover = flag; }
	public void setEarphoneUser(boolean flag) { this.earphone_user = flag; }
	public void setEventLover(boolean flag) { this.event_lover = flag; }
	public void setTakeoutLover(boolean flag) { this.takeout_lover = flag; }
	public void setTags(String[] tags) { this.tags = tags; }
	public void setOpenScore(int val) { this.openScore = val; }
	public void setRespScore(int val) { this.respScore = val; }
	public void setExtrScore(int val) { this.extrScore = val; }
	public void setAgreScore(int val) { this.agreScore = val; }
	public void setNeurScore(int val) { this.neurScore = val; }
	
	public String getUserName() { return userName; }
	public boolean getGamingLover() { return gaming_lover; }
	public boolean getEarphoneUser() { return earphone_user; }
	public boolean getEventLover() { return event_lover; }
	public boolean getTakeoutLover() { return takeout_lover; }
	public String[] getTags() { return tags; }
	public int getOpenScore() { return openScore; }
	public int getRespScore() { return respScore; }
	public int getExtrScore() { return extrScore; }
	public int getAgreScore() { return agreScore; }
	public int getNeurScore() { return neurScore; }
}
