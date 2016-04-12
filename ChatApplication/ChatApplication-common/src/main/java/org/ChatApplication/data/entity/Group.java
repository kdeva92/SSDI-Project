package org.ChatApplication.data.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
* 
* @author Komal
*
*/
@Entity
@Table(name = "group1")
public class Group {

@Id
@GeneratedValue
@Column(name = "group_id")
private int grpouId;

@Column(name = "name")
private String name;

@ManyToMany(fetch = FetchType.LAZY, mappedBy = "groups")
private List<User> members = new ArrayList<User>();

public int getGrpouId() {
return grpouId;
}

public void setGrpouId(int grpouId) {
this.grpouId = grpouId;
}

public String getName() {
return name;
}

public void setName(String name) {
this.name = name;
}

public List<User> getMembers() {
return members;
}

public void setMembers(List<User> members) {
this.members = members;
}

}