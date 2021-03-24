package org.hse.example.service;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.IntStream;

/**
 * Находит минимальное расстояние между счастливыми билетами
 */
public class NearestTickets implements TicketService {
    private int maxNumber;
    private int[] digits;
    private boolean done = false;
    private int ticket = 0;
    private int distance;
    private Optional<Predicate<Integer>> optionalCondition = Optional.empty();
    private Optional<Comparator<Integer>> optionalComparator = Optional.empty();

    /**
     * @param digitsQnty количество цифр в билете
     */
    public NearestTickets(int digitsQnty) {
        if (digitsQnty <= 0 || digitsQnty % 2 != 0) {
            throw new IllegalArgumentException("Передан некорректный параметр! " + digitsQnty);
        }
        // Заполняем атрибуты
        this.maxNumber = (int) (Math.pow(10, digitsQnty) - 1);
        this.digits = new int[digitsQnty];
        this.distance = this.maxNumber;
    }

    /**
     * @param digitsQnty количество цифр в билете
     * @param condition  дополнительное условие
     */
    public NearestTickets(int digitsQnty, Predicate<Integer> condition) {
        this(digitsQnty);
        this.optionalCondition = Optional.ofNullable(condition);
    }

    public NearestTickets(final Integer digitsQty,
                          Predicate<Integer> condition,
                          Comparator<Integer> comparator) {
        this(digitsQty, condition);
        //condition = condition.negate(); // возможно только если condition не final
        this.optionalComparator = Optional.of(comparator);
    }
    /**
     * Выполняет необходимые вычисления
     *
     * @return экземпляр {@link TicketService}
     */
    @Override
    public TicketService doWork() {
        if(done){
            throw new IllegalStateException("Уже выполнено!");
        }

        IntStream
                .rangeClosed(1, maxNumber)
                .filter(number ->
                        this.optionalCondition
                            .map(value -> value.test(number))
                            .orElse(true))
                .filter(this::isLucky)
                .forEach(this::processNumber);

        done = true;
        return this;
    }

    /**
     * Обрабатывает очередной номер
     *
     * @param number номер
     */
    private void processNumber(int number) {
        int currentDistance = number - ticket;
        if (optionalComparator
                .map(value -> value.compare(currentDistance, distance))
                .orElseGet(() -> currentDistance - distance) < 0) {
        //if (currentDistance < distance) {
            distance = currentDistance;
            ticket = number;
        }
    }

    /**
     * @param ticket номер проверяемого билета
     * @return true, если билет счастливый
     */
    private boolean isLucky(int ticket) {
        Arrays.fill(this.digits, 0);
        for (int i = 0, nextNumber = ticket; nextNumber > 0; nextNumber /= 10, i++) {
            this.digits[i] = nextNumber % 10;
        }

        int firstSum =
                Arrays
                    .stream(this.digits, 0, this.digits.length / 2)
                    .sum();
        int lastSum =
                Arrays
                    .stream(this.digits, this.digits.length / 2, this.digits.length)
                    .sum();

        return lastSum == firstSum;
    }

    /**
     * Выводит результат работы объекта
     */
    @Override
    public void printResult() {
        if (!done) {
            throw new IllegalStateException("Нечего выводить!");
        }

        String formattedTicket = "%0" + this.digits.length + "d\t";
        System.out.printf("Минимальное расстояние %d\t" + formattedTicket + formattedTicket,
                this.distance,
                this.ticket,
                (this.ticket - this.distance));
    }
}
