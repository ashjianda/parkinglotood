/* ParkingLot class
    - .spots - list of parking spots
    - park() - park vehicle in spot if vehicle can fit
    - unpark() - unpark vehicle if in spot
    - getOpenSpots() - get number of open spots
*/
import java.util.HashSet;
public class ParkingLot {
    private ParkingSpot[] spots;
    private HashSet<String> licensePlates = new HashSet<>();

    public ParkingLot(int numSpots) {
        this.spots = new ParkingSpot[numSpots];
        for (int i = 0; i < numSpots; i++) {
            VehicleType type = (i % 3 == 0) ? VehicleType.MOTORCYCLE : (i % 3 == 1) ? VehicleType.CAR : VehicleType.BUS;
            this.spots[i] = new ParkingSpot(type);
        }
    }

    public boolean park(Vehicle vehicle) {
        for (ParkingSpot spot : spots) {
            if (licensePlates.contains(vehicle.getLicensePlate())) {
                return false;
            }
            
            if (spot.canFitVehicle(vehicle)) {
                spot.park(vehicle);
                licensePlates.add(vehicle.getLicensePlate());
                return true;
            }
        }

        return false;
    }

    public boolean unpark(Vehicle vehicle) {
        for (ParkingSpot spot : spots) {
            if (!spot.isEmpty() && spot.getVehicle().getLicensePlate().equals(vehicle.getLicensePlate())) {
                spot.unpark();
                licensePlates.remove(vehicle.getLicensePlate());
                return true;
            }
        }

        return false;
    }

    public int getOpenSpots() {
        int count = 0;
        for (ParkingSpot spot : spots) {
            if (spot.isEmpty()) {
                count++;
            }
        }
        return count;
    }

    public ParkingSpot[] getSpots() {
        return spots;
    }
}