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

    // booking
    public static final String BOOKINGS = API + VERSION + "/bookings";
    public static final String CUSTOMER_BOOKINGS = "/customer-bookings";

    // seat
    public static final String SEATS = API + VERSION + "/seats";
    public static final String FLIGHT_SEATS = "/flight-seats";
    public static final String SELECT_SEAT = "/select";

    // calendar
    public static final String CALENDAR = API + VERSION + "/calendar";

    // miles
    public static final String MILES = API + VERSION + "/miles";
    public static final String MILES_TRANSACTIONS = "/transactions";

    // ancillary
    public static final String ANCILLARY = API + VERSION + "/ancillary";

    // check-in
    public static final String CHECKIN = API + VERSION + "/checkin";

    // flight detail
    public static final String FLIGHT_DETAIL = "/detail";

}
