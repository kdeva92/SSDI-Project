package org.ChatApplication.data.entity;

import java.util.ArrayList;

public class GroupVO {
	private String groupName;
	private ArrayList<String> listOfMembers;
	private int groupId;
	
	public GroupVO(){
		
	}
	
	public GroupVO(String groupName,ArrayList<String> listOfMembers) {
		this.groupName = groupName;
		this.listOfMembers = listOfMembers;
		
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public ArrayList<String> getListOfMembers() {
		return listOfMembers;
	}

	public void setListOfMembers(ArrayList<String> listOfMembers) {
		this.listOfMembers = listOfMembers;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	
	
	
	
}
