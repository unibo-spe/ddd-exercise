package it.unibo.ddd.di;

public class Main {
    public static void main(String[] args) {
        var lot = new ParkingLot();
        lot.enter("AB123CD");
        lot.exit("AB123CD");
    }
}
