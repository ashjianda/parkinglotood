import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Function;
public class ParkingLotTest {

    @Test
    public void testBus() {
        ParkingSpot spot = new ParkingSpot(VehicleType.BUS);
        Bus b1 = new Bus("B01");
        assertEquals("B01", b1.getLicensePlate());
        assertTrue(b1.canFitInSpot(spot));
    }

    @Test
    public void testCar() {
        ParkingSpot spot = new ParkingSpot(VehicleType.CAR);
        Car c1 = new Car("C01");
        assertEquals("C01", c1.getLicensePlate());
        assertTrue(c1.canFitInSpot(spot));
    }

    @Test
    public void testMotorcycle() {
        ParkingSpot spot = new ParkingSpot(VehicleType.MOTORCYCLE);
        Motorcycle m1 = new Motorcycle("M01");
        assertEquals("M01", m1.getLicensePlate());
        assertTrue(m1.canFitInSpot(spot));
    }

    @Test
    public void testGetType() {
        Motorcycle m1 = new Motorcycle("M01");
        assertEquals(VehicleType.MOTORCYCLE, m1.getType());
    }

    @Test
    public void testDuplicatePlateParkedCar() {
        ParkingSpot spot = new ParkingSpot(VehicleType.CAR);
        Car c1 = new Car("C01");
        Car c2 = new Car("C01");
        assertTrue(spot.canFitVehicle(c1));
        spot.park(c1);
        assertFalse(spot.canFitVehicle(c2));
    }

    @Test
    public void testGetOpenSpots() {
        ParkingLot lot = new ParkingLot(3);
        Motorcycle m1 = new Motorcycle("M01");
        lot.park(m1);
        assertEquals(2, lot.getOpenSpots());
    }

    @Test
    public void testDuplicateLicensePlate() {
        ParkingLot lot = new ParkingLot(3);
        Motorcycle m1 = new Motorcycle("M01");
        Motorcycle m2 = new Motorcycle("M01");
        assertTrue(lot.park(m1));
        assertFalse(lot.park(m2));
    }

    @Test
    public void testMotorcycleParksAnywhere() {
        ParkingLot lot = new ParkingLot(3);
        Motorcycle m1 = new Motorcycle("M01");
        assertTrue(lot.park(m1));
    }

    @Test
    public void testCarAndBusCannotParkInMotorcycleSpot() {
        ParkingLot lot = new ParkingLot(1);
        Car c1 = new Car("C01");
        Bus b1 = new Bus("B01");
        assertFalse(lot.park(c1));
        assertFalse(lot.park(b1));
    }

    @Test 
    public void testUnpark() {
        ParkingLot lot = new ParkingLot(3);
        Motorcycle m1 = new Motorcycle("M01");
        lot.park(m1);
        assertTrue(lot.unpark(m1));
        assertEquals(3, lot.getOpenSpots());
    }

    @Test
    public void testUnparkFakeVehicle() {
        ParkingLot lot = new ParkingLot(3);
        Motorcycle m1 = new Motorcycle("M01");
        Motorcycle m2 = new Motorcycle("M02");
        lot.park(m1);
        assertFalse(lot.unpark(m2));
    }

    @Test 
    public void testDoublePark() {
        ParkingLot lot = new ParkingLot(3);
        Motorcycle m1 = new Motorcycle("M01");
        assertTrue(lot.park(m1));
        assertFalse(lot.park(m1));  
    }

    @Test
    public void testFullLot() {
        ParkingLot lot = new ParkingLot(3);
        Motorcycle m1 = new Motorcycle("M01");
        Car c1 = new Car("C01");
        Bus b1 = new Bus("B01");
        assertTrue(lot.park(m1));
        assertTrue(lot.park(c1));
        assertTrue(lot.park(b1));
        assertEquals(0, lot.getOpenSpots());
    }

    @Test
    public void testRepark() {
        ParkingLot lot = new ParkingLot(3);
        Car c1 = new Car("C01");
        assertTrue(lot.park(c1));
        assertTrue(lot.unpark(c1));
        assertTrue(lot.park(c1));
        assertEquals(2, lot.getOpenSpots());
    }

    @Test
    public void testParkOccupied() {
        ParkingSpot spot = new ParkingSpot(VehicleType.MOTORCYCLE);
        Motorcycle m1 = new Motorcycle("M01");
        Car c1 = new Car("C01");
        spot.park(m1);
        spot.park(c1);
        assertEquals("M01", spot.getVehicle().getLicensePlate());
    }

    // Fuzzer
    public abstract class FuzzerTest {
        public enum Outcome {
            PASS,
            FAIL,
        }
    
        FuzzerTest() { }
    
        public abstract Vehicle fuzz();
    
        public Outcome run(Function<Vehicle,Outcome> runner) {
            return runner.apply(fuzz());
        }
    
        public Outcome[] runs(Function<Vehicle,Outcome> runner, int trials) {
            Outcome[] outcomes = new Outcome[trials];
            for(int i=0;i<trials;i++) {
                outcomes[i] = runner.apply(fuzz());
            }
            return outcomes;
        }
    }

    public class ParkingLotFuzzer extends FuzzerTest {
        private final Random rand = new Random();

        @Override
        public Vehicle fuzz() {
            String plate = "P" + rand.nextInt(10000);
            int type = rand.nextInt(3); 

            return switch (type) {
                case 1 -> new Car(plate);
                case 2 -> new Bus(plate);
                default -> new Motorcycle(plate);
            };
        }
    }

    private boolean hasCompatibleSpot(Vehicle vehicle, ParkingLot lot) {
        for (ParkingSpot spot : lot.getSpots()) {
            if (spot.canFitVehicle(vehicle)) {
                return true;
            }
        }
        return false;
    }
    

    @Test
    public void fuzzingParkingLotTest() {
        ParkingLot lot = new ParkingLot(100);
        ParkingLotFuzzer fuzzer = new ParkingLotFuzzer();

        FuzzerTest.Outcome[] outcomes = fuzzer.runs(vehicle -> {
            try {
                boolean result = lot.park(vehicle);

                if (!result && hasCompatibleSpot(vehicle, lot)) {
                    return FuzzerTest.Outcome.FAIL;
                }
                return FuzzerTest.Outcome.PASS;
            } catch (Exception e) {
                return FuzzerTest.Outcome.FAIL;
            }
        }, 500);
        assert(Arrays.stream(outcomes)
                .filter(x -> x.equals(FuzzerTest.Outcome.FAIL))
                .count() == 0);
    }
}
