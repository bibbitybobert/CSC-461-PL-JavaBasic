package book_robert;
import java.util.ArrayList;

/*CHECKLIST
    Additional OOP  requirements
    toString properly extended				            _____
    Constructors properly handled			            _____
    Access properly handled (code style requirement)	_____

    Last tier completed:                                ______

 */
/**
 * PARKING LOT API!
 * This program is an API for a district of parking lots to help track the profits,
 * size, number of vehicles and overall condition of various free and paid parking lots in
 * a district. There are three classes, A district class to keep track of all of the parking lots,
 * a ParkingLot class to keep track of what is happening in a specific parking lot, and a
 * FreeParkingLot derived from the base ParkingLot class to keep track of what is happening
 * in another type of parking lot that does not charge an hourly fee.
 * @author Robert Book
 */
public class District {
    //VARIABLES

    /**
     * An array list of all the parking lots that belong to this district
     */
    private ArrayList<ParkingLot> ParkingLotsArray = new ArrayList<ParkingLot>();

    /**
     * Total time all the parking lots in the district are closed at the same time
     */
    private int TotalMinsClosed = 0;

    /**
     * The start time at which all the parking lots are closed at the same time
     */
    private int AllClosedAt;

    //ALL FUNCTIONS
    //GET FUNCTIONS

    //GET TOTAL CLOSED MINUTES

    /**
     * Function to return the total amount of time that all the parking lots in the district
     * are closed AT THE SAME TIME
     * @return          total amount of time all the parking lots in the district are closed
     *                  AT THE SAME TIME
     */
    public int getClosedMinutes(){return TotalMinsClosed;}

    //GET LOT

    /**
     * Function to return a single parking lot from the array of parking lots based on the ID of
     * the parking lot
     * @param ID        the id of the parking lot we want to get
     * @return          the parking lot with the matching ID
     */
    public ParkingLot getLot(int ID){
        return ParkingLotsArray.get(ID);
    }

    //ALL OTHER FUNCTIONS WITH ACTUAL CALCULATIONS

    //DISTRICT MARK VEHICLE ENTRY

    /**
     * Function to mark a vehicle entering a specific parking lot.
     * This function also actively calculates the total time all the parking lots are closed at the
     * same time every time it is called. It can mark when all the lots are closed and add to the counter
     * if it is unchanged when the function is called.
     * This function calls the markVehicleEntry function from the ParkingLot class
     * @param parking_lot_index     the index of the parking lot that the vehicle is entering
     * @param time_since_lot_open   the total time since the district has opened
     * @return                      the ID of the vehicle
     * @see ParkingLot#markVehicleEntry(int)
     */
    public int markVehicleEntry(int parking_lot_index, int time_since_lot_open){
        boolean all_were_closed = isClosed();
        int ID = ParkingLotsArray.get(parking_lot_index).markVehicleEntry(time_since_lot_open);
        if(isClosed()){
            if(all_were_closed)
                TotalMinsClosed += (time_since_lot_open - AllClosedAt);
            AllClosedAt = time_since_lot_open;
        }

        return ID;

    }

    //DISTRICT MARK VEHICLE EXIT

    /**
     * Function to mark a vehicle exiting a specific parking lot in the district.
     * This function actively calculates the total time that all the parking lots are closed
     * at the same time as well as can stop the time if the specified lot opens up upon the exit
     * of the given vehicle. This function calls the markVehicleExit function from the ParkingLot
     * class.
     * @param parking_lot_index     Index of the parking lot being left by the vehicle
     * @param time_since_lot_open   Total time since the district opened
     * @param vehicle_id            ID of the vehicle leaving the specified lot
     * @see ParkingLot#markVehicleExit(int, int)
     */
    public void markVehicleExit(int parking_lot_index, int time_since_lot_open, int vehicle_id){
        if(isClosed())
            TotalMinsClosed += (time_since_lot_open - AllClosedAt);
        ParkingLotsArray.get(parking_lot_index).markVehicleExit(time_since_lot_open, vehicle_id);
    }

    //DISTRICT IS CLOSED

    /**
     * Function to check if all the parking lots in the district are closed at the same time.
     * @return      true if all are closed all at once, false if not
     * @see ParkingLot#isClosed()
     */
    public boolean isClosed(){
        boolean closed = true;
        for (ParkingLot parkingLot : ParkingLotsArray) {
            if (!parkingLot.isClosed())
                closed = false;

        }
        return closed;
    }

    //DISTRICT ADD FUNCTION

    /**
     * Function to add a new parking lot to the array list of all the parking lots in the district
     * @param new_lot   a ParkingLot to add to the array list of lots in the district
     * @return          index of the parking lot that got added or -1 if there was an error
     */
    public int add(ParkingLot new_lot){
        ParkingLotsArray.add(new_lot);
        for(int i = 0; i < ParkingLotsArray.size(); i++)
            if(ParkingLotsArray.get(i) == new_lot)
                return i;

        return -1; //returns -1 if error
    }

    //GET VEHICLES PARKED IN DISTRICT

    /**
     * Function to calculate the total number of vehicles parked in all the parking lots
     * in the district at the time of this function being called.
     * @return      the number of vehicles in the district
     */
    public int getVehiclesParkedInDistrict(){
        int total_vehicles = 0;
        for (ParkingLot parkingLot : ParkingLotsArray) {
            total_vehicles += parkingLot.getVehiclesInLot();
        }
        return total_vehicles;
    }

    //GET TOTAL MONEY COLLECTED ACROSS DISTRICT

    /**
     * Function to return the total profits gathered from all the parking lots in the district
     * at the time of this function being called.
     * @return      the total profit of the district at the time of this function being called
     */
    public double getTotalMoneyCollected(){
        double total_profit = 0;
        for (ParkingLot parkingLot : ParkingLotsArray) {
            total_profit += parkingLot.getProfit();
        }
        return total_profit;
    }

    /**
     * Function to return the data of all the parking lots in the district.
     * Calls the toString function for all the lots in the district and concatenates
     * all of their data into one string to be output.
     * @return      the data from all the parking lots in the district
     * @see ParkingLot#toString()
     */
    //TO STRING
    public String toString() {
        String out = "District status:\n";
        for (ParkingLot parkingLot : ParkingLotsArray) {
            out += (parkingLot.toString() + "\n");
        }

        return out;
    }

}
