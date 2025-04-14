/* ParkingSpot class
    - .vehicle - vehicle in spot
    - .type - largest vehicle that can fit in spot
    - isEmpty() - is spot empty
    - canFitVehicle() - can vehicle fit in spot and is empty
    - park() - park vehicle in spot if vehicle can fit
    - unpark() - unpark vehicle
*/
public class ParkingSpot {
    private Vehicle vehicle;
    private VehicleType type;

    public ParkingSpot(VehicleType type) {
        this.type = type;
        this.vehicle = null;
    }

    public boolean isEmpty() {
        return vehicle == null;
    }

    public boolean canFitVehicle(Vehicle vehicle) {
        return vehicle.canFitInSpot(this) && this.isEmpty();
    }

    public void park(Vehicle vehicle) {
        if(canFitVehicle(vehicle)){
            this.vehicle = vehicle;
        }
    }

    public void unpark() {
        this.vehicle = null;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public VehicleType getType() {
        return type;
    }
}