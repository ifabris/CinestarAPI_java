/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

/**
 *
 * @author Ivan
 */
public class Director extends Person{

    public Director(String firstName, String lastName) {
        super(firstName, lastName);
    }
    
    public Director(String firstName, String lastName, int type) {
        super(firstName, lastName, type);
    }

    public Director(int id, String firstName, String lastName, int type) {
        super(id, firstName, lastName, type);
    }
    

    public Director() {
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getType() {
        return type;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    @Override
    public String toString() {
        return firstName + " " + lastName;
    }
    
    
    
}
