package com.arshmed.ticketreservation.constants;

public class RestApiList {

    public static final String API = "/api";
    public static final String VERSION = "/v1";

    // aircraft
    public static final String AIRCRAFT = API + VERSION + "/aircraft";
    public static final String TAIL_NUMBER = "/tail-number";

    // flight
    public static final String FLIGHTS = API + VERSION + "/flights";
    public static final String AIRCRAFT_FLIGHTS = "/aircraft-flights";
    public static final String AIRPORT_FLIGHTS = "/airport-flights";

    // airport
    public static final String AIRPORTS = API + VERSION + "/airports";
    public static final String AIRPORT_CODE = "/airport-code";

}
