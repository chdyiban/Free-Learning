package yiban.dorm.beans;

public class TagInfo {

	private String tagName;
	private int openScore;
	private int respScore;
	private int extrScore;
	private int agreScore;
	private int neurScore;
	
	public void setTagName(String name) { this.tagName = name; }
	public void setOpenScore(int val) { this.openScore = val; }
	public void setRespScore(int val) { this.respScore = val; }
	public void setExtrScore(int val) { this.extrScore = val; }
	public void setAgreScore(int val) { this.agreScore = val; }
	public void setNeurScore(int val) { this.neurScore = val; }

	public String getTagName() { return tagName; }
	public int getOpenScore() { return openScore; }
	public int getRespScore() { return respScore; }
	public int getExtrScore() { return extrScore; }
	public int getAgreScore() { return agreScore; }
	public int getNeurScore() { return neurScore; }
	
}
