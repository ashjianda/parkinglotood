// Car class - fits in car or bus spots
public class Car extends Vehicle {
    public Car(String licensePlate){
        super(licensePlate, VehicleType.CAR);
    }

    @Override
    public boolean canFitInSpot(ParkingSpot spot) {
        return spot.getType() != VehicleType.MOTORCYCLE;
    }
}