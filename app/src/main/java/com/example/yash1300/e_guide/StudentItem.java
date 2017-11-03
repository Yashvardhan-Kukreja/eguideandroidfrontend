package com.example.yash1300.e_guide;

import java.util.List;

/**
 * Created by Yash 1300 on 29-10-2017.
 */

public class StudentItem {
    String firstName, lastName, age, email, password;
    List<String> skills, teachersInterested;

    public StudentItem(String firstName, String lastName, String age, String email, String password, List<String> skills, List<String> teachersInterested) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.password = password;
        this.skills = skills;
        this.teachersInterested = teachersInterested;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getSkills() {
        return skills;
    }

    public List<String> getTeachersInterested() {
        return teachersInterested;
    }
}
