// Bus class - fits in bus spots
public class Bus extends Vehicle {
    public Bus(String licensePlate){
        super(licensePlate, VehicleType.BUS);
    }

    @Override
    public boolean canFitInSpot(ParkingSpot spot) {
        return spot.getType() == VehicleType.BUS;
    }
}