public class Car {
    // "final" eklendi: Bu değerler constructor'dan sonra değişmez.
    private final String brand;
    private final String model;
    private final int year;
    private final double pricePerDay;

    public Car(String brand, String model, int year, double pricePerDay) {
        this.brand = brand;
        this.model = model;
        this.year = year;
        this.pricePerDay = pricePerDay;
    }

    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getYear() { return year; }
    public double getPricePerDay() { return pricePerDay; }

    @Override
    public String toString() {
        return brand + " " + model + " (" + year + ") - " + pricePerDay + " $";
    }
}