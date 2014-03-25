

public class Flight implements Comparable<Flight> {

	public String destination;
	public String departure;
	int UTCDiffOrign;
	public String arrival;
	int UTCDiffDestination;
	public String DaysOfOperation;
	

	public Flight(String destination, String departure, int UTCDiffOrign, String arrival, int UTCDiffDestination, String daysOfOperation) {
		this.destination = destination;
		this.departure = departure;
		this.UTCDiffOrign =  UTCDiffOrign;
		this.arrival = arrival;
		this.UTCDiffDestination = UTCDiffDestination;
		this.DaysOfOperation = daysOfOperation;
	}

	
	@Override
	public int compareTo(Flight o) {
		if (o.getArrivalTime() < this.getArrivalTime()) {
			return 1;
		} else if (o.getArrivalTime() > this.getArrivalTime()) {
			return -1;
		}

		return 0;
	}

	public Integer getArrivalTime() {
		String[] des = arrival.split(":");
		int destinationMin = (Integer.parseInt(des[0]) * 60) + Integer.parseInt(des[1]) + UTCDiffOrign;
		return destinationMin;
	}
	
	public Integer getDepatureTime() {
		String[] arr = departure.split(":");
		int arrivalMin = (Integer.parseInt(arr[0]) * 60) + Integer.parseInt(arr[1]) + UTCDiffDestination;
		return arrivalMin;
	}
	
	public Integer getFlightDuration(){
		int duration = getArrivalTime() - getDepatureTime();
		if(duration < 0){
			duration += 24*60;
		}
		return duration;
	}
	
}
