package com.example.yash1300.e_guide;

import java.util.List;

/**
 * Created by Yash 1300 on 29-10-2017.
 */

public class TeacherItem {
    String firstName, lastName, age, email, contact;
    List<String> skills, studentsInterested;

    public TeacherItem(String firstName, String lastName, String age, String email, String contact,List<String> skills, List<String> studentsInterested) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.email = email;
        this.contact = contact;
        this.skills = skills;
        this.studentsInterested = studentsInterested;
    }

    public String getContact() {
        return contact;
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

    public List<String> getSkills() {
        return skills;
    }

    public List<String> getStudentsInterested() {
        return studentsInterested;
    }
}
