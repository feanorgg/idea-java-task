import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

public class App {
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        TicketsWrapper wrapper = mapper.readValue(new File("tickets.json"), TicketsWrapper.class);

        Ticket[] tickets = wrapper.tickets;

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd.MM.yy H:mm");

        Map<String, Long> minFlightTimes = new HashMap<>();

        for (Ticket t : tickets) {
            ZonedDateTime dep = ZonedDateTime.of(
                java.time.LocalDateTime.parse(t.departure_date + " " + t.departure_time, fmt),
                ZoneId.of("Asia/Vladivostok")
            );

            ZonedDateTime arr = ZonedDateTime.of(
                java.time.LocalDateTime.parse(t.arrival_date + " " + t.arrival_time, fmt),
                ZoneId.of("Asia/Jerusalem")
            );

            long flightMinutes = Duration.between(dep, arr).toMinutes();
            minFlightTimes.merge(t.carrier, flightMinutes, Math::min);
        }

        System.out.println("Минимальное время полета Владивосток > Тель-Авив по авиакомпаниям:");
        for(String carrier : minFlightTimes.keySet()) {
            long minutes = minFlightTimes.get(carrier);
            System.out.println(carrier + ": " + (minutes/60) + " ч " + (minutes%60) + " мин");
        }

        List<Integer> prices = Arrays.stream(tickets)
            .map(t -> t.price)
            .collect(Collectors.toList());

        double avg = prices.stream().mapToDouble(Integer::doubleValue).average().orElse(0.0);

        Collections.sort(prices);
        double median;
        int n = prices.size();
        if(n%2 == 1) {
            median = prices.get(n/2);
        } else {
            median = (prices.get(n/2 - 1) + prices.get(n/2))/2.0;
        }

        System.out.println("\nСредняя цена: " + avg);
        System.out.println("Медианная цена: " + median);
        System.out.println("Разница: " + (avg-median));
    }
}
