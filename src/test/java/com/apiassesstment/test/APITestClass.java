package com.apiassesstment.test;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.apiautomation.utility.ExtentReportsUtility;
import com.apiautomation.utility.Log4JUtility;
import com.apiautomation.utility.PropertiesUtility;

import com.pojoclass.test.CreateUserPOJO;
import com.pojoclass.test.EmployeeResponseForCreateUser;
import static org.hamcrest.Matchers.*;
import static  io.restassured.RestAssured.*;
import static org.hamcrest.MatcherAssert.assertThat;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

public class APITestClass {
	private int createdUserId;
	String extractedtoken=null;
	PropertiesUtility prop=new PropertiesUtility();
	Properties applicationPro=prop.loadFile("configproperties");
	Log4JUtility log=new Log4JUtility ();
	protected ExtentReportsUtility report=ExtentReportsUtility.getInstance();
	@BeforeClass
	public void init() {
		
		String URI=applicationPro.getProperty("uri");
	 RestAssured.baseURI=URI;
	 report.logTestInfo("URL is followed by ");
	 
	}
	
	@Test
	 public void getAllRecords() {
		String employeeData = applicationPro.getProperty("resourceendpoint");
		Response res = RestAssured.given()
		        .when()
		        .get(employeeData);
					 
					res
					.then()
					.statusCode(200)
					.contentType(ContentType.JSON)
					.time(lessThan(20000L))
					.body("status", is("success"));
					System.out.println("total number of records="+res.body().jsonPath().get("size()"));	
				    res.prettyPrint();
				    report.logTestInfo ("get All Records");
	}
	@Test
public void createUser() {
		 
    String createData = applicationPro.getProperty("resourceendpoint1");
    String nameofUser = applicationPro.getProperty("name");
    String salaryofUser = applicationPro.getProperty("salary");
    String ageofUser = applicationPro.getProperty("age");

    CreateUserPOJO user = new CreateUserPOJO();
    user.setName(nameofUser);
    user.setSalary(salaryofUser);
    user.setAge(ageofUser);

    Response res = RestAssured.given()
            .contentType(ContentType.JSON)
            .body(user)
            .when()
            .post(createData);

    res.then().statusCode(200).contentType(ContentType.JSON).body("status", equalTo("success"));

    EmployeeResponseForCreateUser employeeResponse = res.as(EmployeeResponseForCreateUser.class);
    System.out.println("Response: " + employeeResponse);

    String name = employeeResponse.getData().getName();
    String salary = employeeResponse.getData().getSalary();
    String age = employeeResponse.getData().getAge();
    int id = employeeResponse.getData().getId();

    System.out.println("Name: " + name);
    System.out.println("Salary: " + salary);
    System.out.println("Age: " + age);
    System.out.println("ID: " + id);

    res.prettyPrint();
    assertThat(name, equalTo(nameofUser));
    assertThat(salary, equalTo(salaryofUser));
    assertThat(age, equalTo(ageofUser));
    createdUserId =employeeResponse.getData().getId(); ;
    System.out.println("ID from previous test case: " + createdUserId);
}

	@Test(dependsOnMethods = "createUser")
	public void deleteUser() {
		String baseEndpoint = applicationPro.getProperty("uri");
	    String deleteEndpoint = applicationPro.getProperty("deleteendpoint1");
	    int idToDelete = createdUserId;    // Retrieve the ID from the previous test case

	    String endpoint = baseEndpoint + deleteEndpoint + "/" + idToDelete;

	    Response response = RestAssured.given()
	            .when()
	            .delete(endpoint);

	    response.then().statusCode(200).contentType(ContentType.JSON).body("status", equalTo("success"));

	    String message = response.jsonPath().getString("message");
	    System.out.println("Message: " + message);
	}
	@Test
	public void deleteWithZeroId() {
	    String baseEndpoint = applicationPro.getProperty("uri");
	    String deleteEndpoint = "/delete/{id}";

	    int idToDelete = 0;
	    String endpoint = baseEndpoint + deleteEndpoint.replace("{id}", String.valueOf(idToDelete));

	    Response response = RestAssured.given()
	            .when()
	            .delete(endpoint);

	    response.then().statusCode(400).contentType(ContentType.JSON).body("status", equalTo("error"));

	    String message = response.jsonPath().getString("message");
	    System.out.println("Message: " + message);
}
	@Test
	public void getUserDetails() {
	    String baseEndpoint = applicationPro.getProperty("uri");
	    String employeeEndpoint = applicationPro.getProperty("employeeendpoint1");

	    int id =2;
	    String endpoint = baseEndpoint + employeeEndpoint.replace("{id}", String.valueOf(id));

	    Response response = RestAssured.given()
	            .when()
	            .get(endpoint);
 
	    response.then().statusCode(200).contentType(ContentType.JSON);
	    System.out.println(response.getBody().asString());
	    String employeeName = response.jsonPath().getString("data.employee_name");
	    String employeeSalary = response.jsonPath().getString("data.employee_salary");
	    String employeeAge = response.jsonPath().getString("data.employee_age");

	    assertThat(employeeName, equalTo("Garrett Winters"));
	    assertThat(employeeSalary, equalTo("170750"));
	    assertThat(employeeAge, equalTo("63"));
	    
	    
	    
	}
	
}
