package com.itheima.model;

/**
 * @ClassName com.itheima.model.User
 * @Description
 * @Author Maid
 * @Date 2020/9/9 0009 23:06
 * @Version v1.0
 */
public class User {
	private Integer id;
	private String name;
	private String address;

	public User(Integer id, String name, String address) {
		this.id = id;
		this.name = name;
		this.address = address;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}
}
