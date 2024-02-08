import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Stack;
import java.util.HashMap;

public class Metro {
    private DirectedGraph<Stop> metroGraph;
    private HashMap<Edge, Road> edgeRoadHashMap;

    public Metro(String filePath) {

        metroGraph = new DirectedGraph<>();
        edgeRoadHashMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            reader.readLine(); // Ignore the header line
            String line;

            double startTime = 0.0, arrivalTime = 0.0; // Başlangıç zamanını tanımla
            Stop startStop = new Stop(" ");
            Stop arrivalStop;
            Stop dummyStartStop, dummyArrivalStop;


            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                Route route = new Route(parts[5].trim(), parts[6].trim());

                if (Integer.parseInt(parts[3]) == 1) {
                    dummyStartStop = new Stop(parts[1].trim().toLowerCase());

                    if (metroGraph.hasVertex(dummyStartStop)) {
                        startStop = getStop(dummyStartStop);
                        startStop.addRoute(route);
                    }
                    else {
                        startStop = dummyStartStop;
                        startStop.addRoute(route);
                        metroGraph.addVertex(startStop);
                    }
                    
                    startTime = Double.parseDouble(parts[2].trim());
                }
                else {
                    dummyArrivalStop = new Stop(parts[1].trim().toLowerCase());
                    arrivalTime = Double.parseDouble(parts[2].trim());
                    double timeInterval = arrivalTime - startTime;

                    Road<Double> road = new Road<Double>(Math.abs(timeInterval), route);

                    if (metroGraph.hasVertex(dummyArrivalStop)) {
                        arrivalStop = getStop(dummyArrivalStop);
                        arrivalStop.addRoute(route);
                    }
                    else {
                        arrivalStop = dummyArrivalStop;
                        arrivalStop.addRoute(route);
                        metroGraph.addVertex(arrivalStop);
                    }

                    metroGraph.addEdge(startStop, arrivalStop, road.getLength());
                    edgeRoadHashMap.put(metroGraph.getEdge(startStop, arrivalStop), road);

                    startStop = arrivalStop;
                    startTime = arrivalTime;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getStationCount() {
        return metroGraph.size();
    }
    public int getEdgeCount() {
        return metroGraph.getEdgeCount();
    }
    public void print()
    {
        metroGraph.print();
    }
    private Stop getStop(Stop stop)
    {
        return metroGraph.getValue(stop);
    }
    public void findDirection()
    {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Origin station: ");
        String originStation = scanner.nextLine();
        System.out.print("\nDestination station: ");
        String destinationStation = scanner.nextLine();
        System.out.print("\nPreferetion: ");
        String preferetion = scanner.nextLine();

        if(preferetion.trim().toLowerCase().equals("Fewer Stops".toLowerCase()))
            getDirectionFewerStops(originStation, destinationStation);
        else if(preferetion.trim().toLowerCase().equals("Minimum Time".toLowerCase()))
            getDirectionMinimumTime(originStation, destinationStation);
        else
            System.out.println("ILLEGAL INPUT FOR PREFERETION");
    }
    public void findDirection(String originStation, String destinationStation, int preferetion)
    {
        if(preferetion == 0)
            getDirectionFewerStops(originStation.trim(), destinationStation.trim());
        else if(preferetion == 1)
            getDirectionMinimumTime(originStation.trim(), destinationStation.trim());
        else
            System.out.println("ILLEGAL INPUT FOR PREFERETION");
    }
    private void getDirectionFewerStops(String originStation, String destinationStation)
    {
        Stop origin =  getStop(new Stop(originStation.trim().toLowerCase()));
        Stop destination = getStop(new Stop(destinationStation.trim().toLowerCase()));
        Stack<Stop> s = new Stack<>();
        int length = metroGraph.getShortestPath(origin, destination, s);

        System.out.println(s.toString());

        System.out.println(directionToString(s));
        System.out.println(length);
    }
    private void getDirectionMinimumTime(String originStation, String destinationStation)
    {
        Stop origin =  getStop(new Stop(originStation.trim().toLowerCase()));
        Stop destination = getStop(new Stop(destinationStation.trim().toLowerCase()));
        Stack<Stop> s = new Stack<>();
        double cost = metroGraph.getCheapestPath(origin, destination, s);

        System.out.println(s.toString());

        System.out.println(directionToString(s));
        System.out.println((int)cost / 60 + "min");
    }

    private String directionToString(Stack<Stop> directionStops)
    {
        String direction = "\n";
        Stop startStop = directionStops.pop();
        Stop arrivalStop = directionStops.peek();

        String prevRouteShortName = edgeRoadHashMap.get(metroGraph.getEdge(startStop, directionStops.peek())).getRoute().getRoute_short_name().toLowerCase();

        int count = 0;
        int directionTime = 0;
        Stop lineFirstStop = startStop;
        Stop lineLastStop = arrivalStop;
        direction += "Line " + prevRouteShortName.toUpperCase() + ":" + "\n"
                +lineFirstStop.getStop_name().toUpperCase() + " - ";

        while (!directionStops.isEmpty()){
            arrivalStop = directionStops.peek();
            Edge edge= metroGraph.getEdge(startStop, arrivalStop);
            String currentRouteShortName = edgeRoadHashMap.get(edge).getRoute().getRoute_short_name().toLowerCase();
            directionTime += edge.getWeight();

            System.out.print(currentRouteShortName + " ");

            if(!currentRouteShortName.equals(prevRouteShortName)){
                lineLastStop = startStop;
                direction += lineLastStop.getStop_name().toUpperCase() + "(" + count + " stations" + ")" + "\n";

                lineFirstStop = lineLastStop;
                direction += "Line " + currentRouteShortName.toUpperCase() + ":" + "\n"
                        +lineFirstStop.getStop_name().toUpperCase() + " - ";
                count = 1;

            }
            else {
                count++;
            }

            startStop = directionStops.pop();
            prevRouteShortName = currentRouteShortName;
        }

        return direction + startStop.getStop_name().toUpperCase() + "(" + count + " stations" + ")" + "\n";
    }
}
