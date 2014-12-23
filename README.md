WaldoApp
========

An Android Application implemented in Java

Author: Tunjay Jafarli

The Waldo application lets you (1) track other users of the Waldo application within a certain geographic area, (2) plots your location and the locations of other users on a map, and (3) uses live bus information from Translink to determine the best bus to use to reach a selected user's location from your current location.

We (me and my partner) implemented:
- Java classes in the waldo.model package that represent buses, bus routes, bus stops, trips and a waldo
- Java classes to parse (both in XML and JSON format) information about bus stops and estimates of when buses will arrive at stops

and added the functionalities to:
- plot the user of the phone on a map according to the user’s GPS location and update this location as the user moves
- interact with the Waldo web service to retrieve the locations of a set of Waldos in response to the “Find Waldos” side menu selection, and plot their locations on the map
- determine a direct bus route between the location of the phone user and a given Waldo (and vice versa) based on live data from Translink or display a dialog box saying that no route is available or that the Waldo is within walking distance (when “Get Route” is chosen)
- if a route is available, plot the route on the map and provide data about the stop number and next bus to depart from the starting stop when clicking on either the bus route at the start or end of the route
- interact with the Waldo web service to receive messages and to display the last set of messages sent to a user of the phone when the user icon is hovered over or clicked upon
- remove all Waldos from the map when the “Clear Waldos” is chosen from the side menu
