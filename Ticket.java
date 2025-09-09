class Ticket {
    public String origin;
    public String origin_name;
    public String destination;
    public String destination_name;
    public String departure_date;
    public String departure_time;
    public String arrival_date;
    public String arrival_time;
    public String carrier;
    public int stops;
    public int price;
}

class TicketsWrapper {
    public Ticket[] tickets;
}
