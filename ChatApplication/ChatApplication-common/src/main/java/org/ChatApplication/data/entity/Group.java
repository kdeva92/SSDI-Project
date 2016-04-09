package org.ChatApplication.data.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 * 
 * @author Komal
 *
 */
@Entity
@Table(name = "group")
public class Group {

	@Id
	@GeneratedValue
	@Column(name = "group_id")
	private int grpouId;

	@Column(name = "name")
	private String name;

	@ManyToMany
	@JoinTable(name = "GROUP_USER", joinColumns = { @JoinColumn(name = "group_id") }, inverseJoinColumns = {
			@JoinColumn(name = "user_id") })
	private Set<User> members = new HashSet<User>();

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

	public Set<User> getMembers() {
		return members;
	}

	public void setMembers(Set<User> members) {
		this.members = members;
	}

}
