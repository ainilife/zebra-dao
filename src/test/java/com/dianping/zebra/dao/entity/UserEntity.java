package com.dianping.zebra.dao.entity;

import java.util.Date;

public class UserEntity {

	private int id;

	private String name;

	private String tel;

	private String email;
	
	private String alias;

	private int role;

	private Date updateTime;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
   public String toString() {
	   return "UserEntity [id=" + id + ", name=" + name + ", tel=" + tel + ", email=" + email + ", alias=" + alias
	         + ", role=" + role + ", updateTime=" + updateTime + "]";
   }
}
