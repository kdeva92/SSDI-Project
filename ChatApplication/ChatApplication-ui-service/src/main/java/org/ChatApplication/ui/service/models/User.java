package org.ChatApplication.ui.service.models;

public class User {

private String ninerID;
private String studentName;
private String emailID;
private String contactNumber;
private String password;

	public User(String id,String name,String email, String contact,String password)
	{
		this.contactNumber = contact;
		this.studentName = name;
		this.emailID = email;
		this.ninerID = id;
		this.password = password;
	}
	
	
	public void setNinerID(String id)
	{
		this.ninerID = id;
	}
	
	public void setStudentName(String name)
	{
		this.studentName = name;
	}
	
	public void setEmailID(String email)
	{
		this.emailID = email;
	}
	
	public void setContactNumber(String contact)
	{
		this.contactNumber = contact;
	}
	
	public void setPassword(String passwd)
	{
		this.password = passwd;
	}
	
	public String getNinerID()
	{
		return this.ninerID;
	}
	
	public String getStudentName()
	{
		return this.studentName;
	}
	
	public String getEmailID()
	{
		return this.emailID;
	}
	
	public String getContactNumber()
	{
		return this.contactNumber;
	}
	
	public String getPassword()
	{
		return this.password;
	}
	
	
	@Override
		public String toString() {
			// TODO Auto-generated method stub
			return this.ninerID+"\t"+this.studentName+"\t"+this.emailID+"\t"+this.contactNumber+"\t"+this.password;
		}

}
