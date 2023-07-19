package com.pojoclass.test;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EmployeeResponseForCreateUser {
   private String status;
   private EmployeeData data;

public EmployeeData getData() {
	return data;
}

public void setData(EmployeeData data) {
	this.data = data;
}

public String getStatus() {
	return status;
}

public void setStatus(String status) {
	this.status = status;
}



}
