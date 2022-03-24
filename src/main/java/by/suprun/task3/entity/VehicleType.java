package by.suprun.task3.entity;

public enum VehicleType {
    CAR(7, 1600),
    TRUCK(19, 17000);

    private int area;
    private int weight;

    VehicleType(int area, int weight) {
        this.area = area;
        this.weight = weight;
    }

    public int getArea() {
        return area;
    }

    public int getWeight() {
        return weight;
    }
}
