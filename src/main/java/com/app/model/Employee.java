package com.app.model;

import javax.persistence.Id;



import lombok.Data;

@Data
@javax.persistence.Entity
public class Employee {
@Id
private String employeeId;
private String firstName;
private String lastName;
private String email;
private int age;

}
