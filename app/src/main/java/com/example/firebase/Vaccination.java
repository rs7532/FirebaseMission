package com.example.firebase;

public class Vaccination {
    public String dateOfVaccination, place;
    public Vaccination(){}

    public Vaccination(String dateOfVaccination, String place) {
        this.place = place;
        this.dateOfVaccination = dateOfVaccination;
    }

    public String getDateOfVaccination() {
        return dateOfVaccination;
    }

    public void setDateOfVaccination(String dateOfVaccination) {
        this.dateOfVaccination = dateOfVaccination;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}
