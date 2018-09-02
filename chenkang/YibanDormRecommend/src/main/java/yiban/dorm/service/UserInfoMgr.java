package yiban.dorm.service;

import org.springframework.beans.factory.annotation.Autowired;

import yiban.dorm.beans.TagInfo;
import yiban.dorm.beans.UserInfo;
import yiban.dorm.dao.TagsRepository;
import yiban.dorm.dao.UserRepository;

public class UserInfoMgr {
	
	@Autowired
	private UserRepository userRepository; // TODO Singleton? Should final?
	
	@Autowired
	private TagsRepository tagsRepository;
	
	public void calculateScores(UserInfo user) {
		for(String tag : user.getTags()) {
			TagInfo tagInfo = tagsRepository.getTagByName(tag);
			user.setOpenScore(user.getOpenScore() + tagInfo.getOpenScore());
			user.setRespScore(user.getRespScore() + tagInfo.getRespScore());
			user.setExtrScore(user.getExtrScore() + tagInfo.getExtrScore());
			user.setAgreScore(user.getAgreScore() + tagInfo.getAgreScore());
			user.setNeurScore(user.getNeurScore() + tagInfo.getNeurScore());
		}
	}
	
	public void addUser(UserInfo user) {
		userRepository.addUser(user);
	}
	
	
}
