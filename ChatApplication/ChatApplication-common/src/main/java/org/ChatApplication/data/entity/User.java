package org.ChatApplication.data.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name = "user")
public class User {

	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private int id;

	@Column(name = "niner_id", nullable = false, unique = true)
	private String ninerId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "password")
	private String password;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "members", cascade = CascadeType.ALL)
	private List<Group> groups = new ArrayList<Group>();

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNinerId() {
		return ninerId;
	}

	public void setNinerId(String ninerId) {
		this.ninerId = ninerId;
	}

	@Override
	public boolean equals(Object arg0) {
		// TODO Auto-generated method stub
		if (!(arg0 instanceof User))
			return false;
		return getNinerId().equals(((User) arg0).getNinerId());
	}

}
