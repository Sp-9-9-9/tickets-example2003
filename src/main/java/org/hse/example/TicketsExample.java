package org.hse.example;

import org.hse.example.service.*;

import java.util.Collections;
import java.util.Comparator;
import java.util.Optional;

/**
 * Реализация примера со счастливыми билетами
 */
public class TicketsExample {

    /**
     * Точка входа
     *
     * @param args строка аргументов. В настоящее время не используется
     */
    public static void main(String[] args) {
        System.out.println("Hello world!");
        //TicketServiceBuilder builder = () -> 2;
        //NearestTicketsBuilder builder = () -> 6;
        //TicketCounterServiceImpl service = new TicketCounterServiceImpl(6, sum -> sum % 3 == 0);

        //Comparator<Integer> comparator = (a1, a2) -> (Math.abs(a1) - Math.abs(a2));
        Comparator<Integer> comparator = (a1, a2) -> {
            int distance = Math.abs(a1 - a2);
            return (int) (distance / 2 - Math.random() * distance);
        };

        //NearestTickets service = new NearestTickets(6, sum -> sum % 41 == 0);
        TicketService service = new NearestTickets(6, sum -> sum % 41 == 0, comparator.reversed());
//        NearestTickets service = new NearestTickets(6, null);
        Optional.of("Hello world!").map(String::length).orElse(0);
        Optional.ofNullable((String)null).map(String::length).orElse(0);

        service.doWork().printResult();
    }

}
