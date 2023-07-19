package com.pojoclass.test;

public class EmployeeData {
	   private String name;
	   private String salary;
	   private String age;
	   private int id;
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
	public String getSalary() {
		return salary;
	}
	public void setSalary(String salary) {
		this.salary = salary;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}


	 @Override
	 public String toString() {
	     return "Employee [name=" + name + ", salary=" + salary + ", age=" + age + ", id=" + id + "]";
	 }
	}
	   
	


