package com.example.firebase;

public class Student {
    public String firstName, lastName, layer, id;
    public int grade;
    public boolean vaccinated;
    public Vaccination firstVaccination, secondVaccination;

    public Student(){}
    public Student(String id, String firstName, String lastName, String layer, int grade, boolean vaccinated, Vaccination firstVaccination, Vaccination secondVaccination) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.layer = layer;
        this.id = id;
        this.grade = grade;
        this.vaccinated = vaccinated;
        this.firstVaccination = firstVaccination;
        this.secondVaccination = secondVaccination;
    }

    public Vaccination getFirstVaccination() {
        return firstVaccination;
    }

    public void setFirstVaccination(Vaccination firstVaccination) {
        this.firstVaccination = firstVaccination;
    }

    public Vaccination getSecondVaccination() {
        return secondVaccination;
    }

    public void setSecondVaccination(Vaccination secondVaccination) {
        this.secondVaccination = secondVaccination;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLayer() {
        return this.layer;
    }

    public void setLayer(String layer) {
        this.layer = layer;
    }

    public int getGrade() {
        return this.grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public boolean isVaccinated() {
        return this.vaccinated;
    }

    public void setVaccinated(boolean vaccinated) {
        this.vaccinated = vaccinated;
    }
}
