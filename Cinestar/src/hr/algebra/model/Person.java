/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import javax.xml.bind.annotation.XmlAttribute;

/**
 *
 * @author Ivan
 */

public class Person implements Comparable<Person>{
    
    @XmlAttribute
    public int  id;
    public String firstName;
    public String lastName;
    public int type;

    public Person() {
    }

    public Person(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person(int id, String firstName, String lastName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person(String firstName, String lastName, int type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
    }

    public Person(int id, String firstName, String lastName, int type) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.type = type;
    }      

    @Override
    public int compareTo(Person t) {
        if (lastName.equals(t.lastName)) {
            return firstName.compareTo(t.firstName);
        }
        return lastName.compareTo(t.lastName);
    }
}
