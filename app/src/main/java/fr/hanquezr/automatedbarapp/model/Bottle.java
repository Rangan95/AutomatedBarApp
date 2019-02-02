package fr.hanquezr.automatedbarapp.model;

public class Bottle {

    private int id;
    private String name;
    private double maxCapacity;
    private double actuCapacity;
    private boolean viscous;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getMaxCapacity() {
        return maxCapacity;
    }

    public void setMaxCapacity(double maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public double getActuCapacity() {
        return actuCapacity;
    }

    public void setActuCapacity(double actuCapacity) {
        this.actuCapacity = actuCapacity;
    }

    public boolean isViscous() {
        return viscous;
    }

    public void setViscous(boolean viscous) {
        this.viscous = viscous;
    }

}
