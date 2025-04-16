package pt.ipp.isep.dei.domain;

import java.util.Objects;

public class Station {
    private String name;
    private int year;
    private int month;
    private int arrivals;
    private int iron;
    private int coal;
    private int steel;
    private int vegetables;
    private int cereals;
    private int wool;
    private int coffee;
    private int cattle;
    private int passengers;
    private int mail;
    private int revenues;
    private int expenses;

    public Station(String name, int year, int month, int arrivals, int iron, int coal, int steel, int vegetables, int cereals, int wool, int coffee, int cattle, int passengers, int mail, int revenues, int expenses) {
        this.name = name;
        this.year = year;
        this.month = month;
        this.arrivals = arrivals;
        this.iron = iron;
        this.coal = coal;
        this.steel = steel;
        this.vegetables = vegetables;
        this.cereals = cereals;
        this.wool = wool;
        this.coffee = coffee;
        this.cattle = cattle;
        this.passengers = passengers;
        this.mail = mail;
        this.revenues = revenues;
        this.expenses = expenses;
    }

    public String getName() {
        return name;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getArrivals() {
        return arrivals;
    }

    public int getIron() {
        return iron;
    }

    public int getCoal() {
        return coal;
    }

    public int getSteel() {
        return steel;
    }

    public int getVegetables() {
        return vegetables;
    }

    public int getCereals() {
        return cereals;
    }

    public int getWool() {
        return wool;
    }

    public int getCoffee() {
        return coffee;
    }

    public int getCattle() {
        return cattle;
    }

    public int getPassengers() {
        return passengers;
    }

    public int getMail() {
        return mail;
    }

    public int getRevenues() {
        return revenues;
    }

    public int getExpenses() {
        return expenses;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setArrivals(int arrivals) {
        this.arrivals = arrivals;
    }

    public void setIron(int iron) {
        this.iron = iron;
    }

    public void setCoal(int coal) {
        this.coal = coal;
    }

    public void setSteel(int steel) {
        this.steel = steel;
    }

    public void setVegetables(int vegetables) {
        this.vegetables = vegetables;
    }

    public void setCereals(int cereals) {
        this.cereals = cereals;
    }

    public void setWool(int wool) {
        this.wool = wool;
    }

    public void setCoffee(int coffee) {
        this.coffee = coffee;
    }

    public void setCattle(int cattle) {
        this.cattle = cattle;
    }

    public void setPassengers(int passengers) {
        this.passengers = passengers;
    }

    public void setMail(int mail) {
        this.mail = mail;
    }

    public void setRevenues(int revenues) {
        this.revenues = revenues;
    }

    public void setExpenses(int expenses) {
        this.expenses = expenses;
    }

    // Considera que uma station é diferente pelo nome.
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, year, month, arrivals, iron, coal, steel, vegetables, cereals, wool, coffee, cattle, passengers, mail, revenues, expenses);
    }
}
