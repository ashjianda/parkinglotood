// Motorcycle class - fits in all spots
public class Motorcycle extends Vehicle {
    public Motorcycle(String licensePlate){
        super(licensePlate, VehicleType.MOTORCYCLE);
    }

    @Override
    public boolean canFitInSpot(ParkingSpot spot) {
        return true;
    }
}