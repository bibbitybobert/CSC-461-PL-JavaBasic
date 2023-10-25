package book_robert;

/**
 * @author Robert Book
 */
public class FreeParkingLot extends ParkingLot{

    //BASE CONSTRUCTOR

    /**
     * Class constructor for the FreeParkingLot, derived from the ParkingLot class
     * with the notable difference being that these Parking Lots do not have an hourly
     * rate to charge the cars upon exiting.
     * @param name          The name of the parking lot
     * @param lot_size      the maximum capacity of the lot
     */
    public FreeParkingLot(String name, int lot_size){
        super(name, lot_size, 0);
    }

    //SINGLE ARG CONSTRUCTOR

    /**
     * Class constructor where only the maximum size of the lot needs to be specified,
     * the name will be defaulted to "test".
     * @param lot_size      The maximum capacity of the lot
     */
    public FreeParkingLot(int lot_size){this("test", lot_size);}

    //MARK VEHICLE EXIT

    /**
     * An overridden function for exiting vehicles that will not calculate the fee they owe upon
     * exiting. While also ignoring the ID as it does not need to be specified
     * @param min_since_lot_open    the total time since the lot has opened, used to calculate the time
     *                              that the lot has been closed/opened
     * @param ID                    the ID of the car exiting the lot, ignored in this function
     * @see markVehicleExit
     */
    @Override
    public void markVehicleExit(int min_since_lot_open, int ID) {
        markVehicleExit(min_since_lot_open);
    }

    /**
     * Function to mark that a vehicle has exited this parking lot. It does not require an ID for the
     * exiting car as there is no need to keep track of the IDs of cars in a free parking lot.
     * @param min_since_lot_open    the total time since the lot has opened, used to calculate the time
     *                              that the lot has been closed/opened
     */
    public void markVehicleExit(int min_since_lot_open) {
        if(min_since_lot_open < LastRecordedTime)
            return;
        if(getVehiclesInLot() == 0)
            return;
        if(isClosed()) {
            ClosedMin += (min_since_lot_open - CloseTime);
            CloseTime = min_since_lot_open;
        }
        CarsInLotData.remove(CarsInLotData.lastElement());

        LastRecordedTime = min_since_lot_open;
        boolean was_closed = isClosed();

    }

}
