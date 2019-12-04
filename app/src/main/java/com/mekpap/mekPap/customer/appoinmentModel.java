package com.mekpap.mekPap.customer;

public class appoinmentModel {
    private String carModel;
    private String carTypr;
    private String CarProblem;
    private String appointmrntDate;

    public appoinmentModel() {
    }

    public appoinmentModel(String carModel, String carTypr, String carProblem, String appointmrntDate) {
        this.setCarModel(carModel);
        this.setCarTypr(carTypr);
        setCarProblem(carProblem);
        this.setAppointmrntDate(appointmrntDate);
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getCarTypr() {
        return carTypr;
    }

    public void setCarTypr(String carTypr) {
        this.carTypr = carTypr;
    }

    public String getCarProblem() {
        return CarProblem;
    }

    public void setCarProblem(String carProblem) {
        CarProblem = carProblem;
    }

    public String getAppointmrntDate() {
        return appointmrntDate;
    }

    public void setAppointmrntDate(String appointmrntDate) {
        this.appointmrntDate = appointmrntDate;
    }
}
