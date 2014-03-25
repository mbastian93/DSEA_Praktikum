
import java.io.*;
import java.util.*;

public class Airports {

	public static HashMap<String, ArrayList<Flight>> Connections = new HashMap<>();
	public static HashMap<String, Integer> FlightDurations = new HashMap<>();


	public static void initConnections() throws FileNotFoundException {

		BufferedReader bf = new BufferedReader(new FileReader(new File("bin\\Flugplan.csv")));
		String line;
		try {
			line = bf.readLine();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Connections = new HashMap<>();
		try {
			while((line = bf.readLine()) != null) {
				String[] raw = line.replaceAll("\"", "").split(";");
				String origin = raw[2];

				Flight dn = new Flight(raw[3], raw [8], Integer.parseInt(raw[10]), raw[9], Integer.parseInt(raw[11]), raw[12]);
				if (Connections.containsKey(origin)){
					Connections.get(origin).add(dn);
				} else {
					ArrayList<Flight> tmp = new ArrayList<>();
					tmp.add(dn);
					Connections.put(origin, tmp);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			bf.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@SuppressWarnings("unused")
	public static String findFastestConnection(String origin, String destination, String day, String depTime) {

		FlightDurations = new HashMap<>();
		for (String dest : Connections.keySet()) {
			FlightDurations.put(dest, Integer.MAX_VALUE);
		}
		FlightDurations.put(origin, 0);


		PriorityQueue<String> destinations =  new PriorityQueue<>(10, new Comparator<String>(){
			@Override
			public int compare(String s1, String s2) {
				if (FlightDurations.get(s1) < FlightDurations.get(s2)) {
					return -1;
				} else if (FlightDurations.get(s1) < FlightDurations.get(s2)) {
					return 1;
				}
				return 0;
			}
		});
		for (String string : FlightDurations.keySet()) {
			destinations.add(string);
		}
		while(!destinations.isEmpty()) {
			//"nextLocation" are all flights starting from "currentLocation"
			String currentLocation = destinations.poll();
			for (Flight nextFlight : Connections.get(currentLocation)) {
				if(!destinations.contains(nextFlight.destination)) {
					continue;
				}
				PriorityQueue<Flight> flights = new PriorityQueue<>();
				for (Flight flight : Connections.get(nextFlight.destination)) {
					flights.add(flight);
				}
				
				if(!flights.isEmpty()) {
					Flight flight = flights.peek();
					//If we need more time to travel with "nextFlight" than we would using "flight" from currentLocation we update the travel time for "nextLocation"
					int timeToCurrent = FlightDurations.get(currentLocation);
					int flightDuration = flight.getFlightDuration();
					int timeToNext = FlightDurations.get(flight.destination);
					if(timeToCurrent + flightDuration < timeToNext) {
						//Debugging
						FlightDurations.put(flight.destination, (timeToCurrent + flightDuration));
						System.out.println(flight.destination + " " +  timeToCurrent + " " +flightDuration);
						System.out.println("Dauer: " + FlightDurations.get(flight.destination));
					}
				}
			}

		}
		/*
		//returns the Date that fits to the flight
		int arrTimeH = FlightDurations.get(destination).getHours();  
		int arrTimeM = FlightDurations.get(destination).getMinutes();
		String weekDay = null; 
		switch(FlightDurations.get(destination).getDay()){
			case 1: weekDay = "MO";
			break;
			case 2: weekDay = "DI";
			break;
			case 3: weekDay = "MI";
			break;
			case 4: weekDay = "DO";
			break;
			case 5: weekDay = "FR";
			break;
			case 6: weekDay = "SA";
			break;
			case 7: weekDay = "SO";
			break;
		}
		 */		
		//return  origin + " " + weekDay + " " +  arrTimeH + ":" + arrTimeM + " " + destination  + " " + "" + " " +  "" + ":" + ""; 
		//ZRH FCO MO 12:00
		if (FlightDurations.containsKey(destination))
			return FlightDurations.get(destination).toString();
		return "Nope";
	}


	public static void main (String args[]){
		try {
			initConnections();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Scanner s = new Scanner(System.in);
		String line;
		while(s.hasNext()){
			line = s.nextLine();
			String[] request = line.split(" ");
			String origin = request[0];
			String destination = request[1];
			String day = request[2];
			String depTime = request[3];

			String connection = findFastestConnection(origin, destination, day, depTime);
			System.out.println(connection);
			//			for (int i = 0; i < connection.length; i++) {
			//				System.out.println(connection[i]);
			//			}

		}
		s.close();
	}


}
