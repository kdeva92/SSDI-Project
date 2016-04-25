package org.ChatApplication.ui.service.models;

import java.util.ArrayList;
import java.util.Arrays;

import org.ChatApplication.data.entity.GroupVO;

import javafx.beans.property.SimpleStringProperty;

public class GroupTableObject {
	int groupID;
	SimpleStringProperty groupName;
	ArrayList<String> members = new ArrayList<String>();
	
	public GroupTableObject(GroupVO group){
		this.groupID = group.getGroupId();
		this.groupName = new SimpleStringProperty(group.getGroupName());
		this.members = group.getListOfMembers();
	}
	
	public GroupTableObject(int id, String name, String list){
		this.groupID = id;
		this.groupName = new SimpleStringProperty(name);
		String[] arrList = list.split(",");
		this.members = new ArrayList<String>(Arrays.asList(arrList));
		
		
	}
	
	
	public int getGroupID() {
		return this.groupID;
	}

	public void setGroupID(int sender) {
		this.groupID = sender;
	}
	
	public String getGroupName() {
		return this.groupName.get();
	}

	public void setGroupName(String sender) {
		this.groupName.set(sender);
	}

	public ArrayList<String> getMembers() {
		return members;
	}

	public void setMembers(ArrayList<String> members) {
		this.members = members;
	}

}

