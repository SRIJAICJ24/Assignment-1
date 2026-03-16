import java.util.*;

public class Problem8 {
    static class Spot {
        String licensePlate;
        long entryTime;
        boolean occupied;
    }

    private final Spot[] table;
    private int size = 0;

    public Problem8(int capacity) {
        table = new Spot[capacity];
        for (int i = 0; i < capacity; i++) table[i] = new Spot();
    }

    private int hash(String licensePlate) {
        return Math.abs(licensePlate.hashCode()) % table.length;
    }

    public String parkVehicle(String licensePlate) {
        if (size == table.length) return "Parking full";
        int index = hash(licensePlate);
        int probes = 0;

        while (table[index].occupied) {
            index = (index + 1) % table.length;
            probes++;
        }

        table[index].licensePlate = licensePlate;
        table[index].entryTime = System.currentTimeMillis();
        table[index].occupied = true;
        size++;
        return "Assigned spot #" + index + " (" + probes + " probes)";
    }

    public String exitVehicle(String licensePlate) {
        int index = hash(licensePlate);
        int checked = 0;

        while (checked < table.length) {
            if (table[index].occupied && licensePlate.equals(table[index].licensePlate)) {
                long durationMillis = System.currentTimeMillis() - table[index].entryTime;
                double hours = durationMillis / 3600000.0;
                double fee = Math.ceil(hours) * 5.0;
                table[index].occupied = false;
                table[index].licensePlate = null;
                size--;
                return "Spot #" + index + " freed, Fee: $" + fee;
            }
            index = (index + 1) % table.length;
            checked++;
        }
        return "Vehicle not found";
    }

    public String getStatistics() {
        double occupancy = size * 100.0 / table.length;
        return "Occupancy: " + String.format("%.2f", occupancy) + "%";
    }

    public static void main(String[] args) {
        Problem8 parking = new Problem8(10);
        System.out.println(parking.parkVehicle("ABC-1234"));
        System.out.println(parking.parkVehicle("ABC-1235"));
        System.out.println(parking.exitVehicle("ABC-1234"));
        System.out.println(parking.getStatistics());
    }
}