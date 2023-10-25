package book_robert;
import java.util.*;

/**
 * @author Robert Book
 */
public class ParkingLot {

    /**
     * Total time the parking lot has been closed
     */
    public int ClosedMin = 0;

    /**
     * Time at which the parking lot closed
     */
    public int CloseTime;

    /**
     * The last recorded time, updated whenever a function that requires the time
     * the parking lot has been opened is called.
     */
    public int LastRecordedTime = 0;

    /**
     * Vector to keep track of all the vehicles in the lot. This vector keeps track
     * of both the ID of each vehicle as well as what time they entered the lot.
     */
    public Vector<Integer[]> CarsInLotData = new Vector<Integer[]>();

    /**
     * The current ID used to keep track of the vehicles in the lot
     */
    private int CurrID;

    /**
     * The name of the lot
     */
    String Name;

    /**
     * The maximum capacity of the lot.
     */
    private final int LotSize;

    /**
     * The hourly fee to park in the lot
     */
    private double Price;

    /**
     * The total amount of profit gathered by this lot since it opened
     */
    private double Profit = 0;

    /**
     * Constant variable for the percentage that a lot must be filled to be considered "Closed"
     */
    public static final double CLOSED_THRESHOLD = 80;

    //GRADING: CONSTRUCTION

    /**
     * Class constructor with all arguments that could be applied.
     * @param new_name          the name of the new parking lot
     * @param new_lot_size      the maximum capacity of this parking lot
     * @param new_fee_order     the price per hour to park in this lot
     */
    public ParkingLot(String new_name, int new_lot_size, double new_fee_order){
        Name = new_name; //set name
        LotSize = new_lot_size; //set lot size
        Price = new_fee_order; //set price per hour
    }

    /**
     * Class constructor requiring only the maximum capacity to be specified,
     * the name will be defaulted to "test" and the fee will be defaulted to
     * $1.00 per hour.
     * @param lot_size          the maximum capacity of this parking lot
     * @see ParkingLot
     */
    public ParkingLot(int lot_size){
        this("test", lot_size ,1.00 );
    }

    /**
     * Class constructor requiring only the maximum capacity and the price per hour to be specified.
     * The name will be defaulted to "test".
     * @param lot_size          the maximum capacity of this parking lot
     * @param new_fee_order     the price per hour to park in this lot
     * @see ParkingLot
     */
    public ParkingLot(int lot_size, double new_fee_order){this("test", lot_size, new_fee_order);}

    /**
     * Class constructor requiring only the name and maximum capacity to be specified.
     * The fee order will be defaulted to $1.00 per hour.
     * @param new_name          the name of the new parking lot
     * @param lot_size          the maximum capacity of this parking lot
     * @see ParkingLot
     */
    public ParkingLot(String new_name, int lot_size){this(new_name, lot_size, 1.00);}

    
    //ALL FUNCTIONS
    //GET FUNCTIONS

    //Num Vehicles in lot
    /**
     * a function to get the current number of vehicles parked in this lot
     * @return number of vehicles currently in the lot
     */
    public int getVehiclesInLot(){
        return CarsInLotData.size();
    }

    //IS AT CAPACITY

    /**
     * a function to check if the parking lot has reached maximum capacity
     * @return true if the parking lot is at maximum capacity, false if not
     */
    public boolean isAtCapacity(){
        return CarsInLotData.size() == LotSize;
    }

    //GET CLOSED MINUTES

    /**
     * a function to get the total amount of time in minutes that this parking lot has been closed
     * @return the total number of time that this lot has been closed
     */
    public int getClosedMinutes(){
        return ClosedMin;
    }

    //GET PROFIT

    /**
     * a function to get the total profit this parking lot has gathered since opening
     * @return a double of the total profit this parking lot has gathered since opening
     */
    public double getProfit(){
        return Profit;
    }

    //GET NAME

    /**
     * a function to get the name of the parking lot
     * @return the name of the parking lot
     */
    public String getName(){ return Name;}

    //ALL OTHER FUNCTIONS WITH CALCULATIONS
    //MARK VEHICLE ENTRY

    /**
     * a function to mark when a vehicle enters the parking lot.
     * it will return the ID of the entering vehicle or -1 if there is an error.
     * this function also will mark when the lot becomes "closed" or reaches maximum capacity
     * while also checking to see if it opens up again every time it is called.
     * This function actively updates the total amount of time the parking lot is closed.
     * @param min_since_lot_open    the total time since the lot has opened, used to calculate time in
     *                              lot for each car, and to calculate closed time
     * @return  ID of the car entering the lot or -1 if an error occurs
     */
    public int markVehicleEntry(int min_since_lot_open){
        if(min_since_lot_open < LastRecordedTime)
            return -1;
        LastRecordedTime = min_since_lot_open;
        boolean was_closed = isClosed();
        if(!isAtCapacity()) { //if the lot is not at capacity
            int ID_out = CurrID;
            Integer[] car_data = {ID_out, min_since_lot_open};
            CarsInLotData.add(car_data);
            CurrID++;
            if(isClosed() && !was_closed)
                CloseTime = min_since_lot_open; //if lot closes after vehicle mark when lot closes
            else if(isClosed() && was_closed) {
                ClosedMin += (min_since_lot_open - CloseTime);
                CloseTime = min_since_lot_open;
            }

            return ID_out;
        }
        return -1; //if lot at capacity, return -1
    }

    //MARK VEHICLE EXIT

    /**
     * this function marks when a vehicle exits the lot.
     * This function calculates if the lot opens up or regains capacity upon the
     * exit of a vehicle.
     * This function also actively calculates the total time that a lot is closed every time it is called
     * as well as gathering the total profits gathered from each car as it leaves
     * @param min_since_lot_open    the total time since the lot has opened, used to calculate time in
     *                              lot for each car, and to calculate closed time
     * @param ID                    the ID of the car exiting the lot, used to calculate the time in the
     *                              lot for this specific car and to gather the profit from said car
     */
    public void markVehicleExit(int min_since_lot_open, int ID){
        if(min_since_lot_open < LastRecordedTime)
            return;
        if(ID < 0)
            return;
        LastRecordedTime = min_since_lot_open;
        if(isClosed()) {
            ClosedMin += (min_since_lot_open - CloseTime);
            CloseTime = min_since_lot_open;
        }

        Integer[] temp = {-1};

        for(int i = 0; i < CarsInLotData.size(); i++){
            temp = CarsInLotData.get(i);
            if(temp[0] == ID) {
                CarsInLotData.removeElementAt(i);
                break;
            }
        }
        if(temp[0] == -1)
            return;

        int vehicle_time_in_lot = min_since_lot_open - temp[1];

        Profit += payment(vehicle_time_in_lot);


    }



    //gets price that exiting vehicles owe

    /**
     * this function calculates how much a vehicle owes upon exiting a lot.
     * If the vehicle spent less than fifteen (15) minutes in the lot, it will not
     * be charged.
     * @param time_in_lot       the amount of time the vehicle spent in the lot
     * @return                  the price of spending the given time in the lot
     */
    public double payment(int time_in_lot){
        if(time_in_lot <= 15){
            return 0;
        }
        else{
            return time_in_lot * (Price/60);
        }
    }



    //GET CAPACITY PERCENT

    /**
     * Calculates how full the lot is at the time it is called, rounded to the nearest 10th of a percent
     * @return                  how full the lot is, rounded to the nearest 10th of a percent
     */
    public double getCapacityPercent(){
        return (double) Math.round((((double)CarsInLotData.size() / (double)LotSize) * 100) * 10)/ 10;
    }

    //TO STRING

    /**
     * This function formats an output string to give data on the lot.
     * The returned string will specify the name of the lot, how many vehicles in the lot, how full the lot is to
     * the nearest 10th of a percent, and how much money has been collected since it has opened.
     * If the lot is closed, instead of specifying how full the lot is, it will simply output "CLOSED" in place
     * of the percentage.
     * If the lot does not have a price, the string returned will not specify how much money has been collected
     * as it will always be "$0.00"
     * @return                  a string with details of the lot
     */
    public String toString(){
        double lot_percent_full = getCapacityPercent();
        String percentage_full;

        if(isClosed())
            percentage_full = "CLOSED";
        else
            percentage_full = String.valueOf(lot_percent_full).replaceFirst("\\.0+$", "") + "%";
        if(Price == 0)
            return String.format("Status for %s parking lot: %d vehicles (%s)",
                    getName(), getVehiclesInLot(), percentage_full);
        else return String.format("Status for %s parking lot: %d vehicles (%s) Money Collected: $%.2f",
                getName(), getVehiclesInLot(), percentage_full, getProfit());
    }

    //IS CLOSED

    /**
     * A function to calculate if the lot has been closed, determined by the constant CLOSED_THRESHOLD.
     * @return              true if the number of cars is CLOSED_THRESHOLD% of the lot or more, false if under CLOSED_THRESHOLD%
     */
    public boolean isClosed(){
        //if CLOSED_THRESHOLD% of lot is full
        return CarsInLotData.size() >= (LotSize * (CLOSED_THRESHOLD / 100));
    }



}
