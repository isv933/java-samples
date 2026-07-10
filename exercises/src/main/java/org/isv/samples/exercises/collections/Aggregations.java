package org.isv.samples.exercises.collections;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

public class Aggregations {
    public record Order (String orderId, double amount, OrderStatus status) {
        public enum OrderStatus {
            CREATED,
            PAYED,
            SUCCESS
        }

        public enum UserStatus {
            ACTIVE,
            DISABLED
        }

        public record User (String Id, String name, String department, double salary, String email, UserStatus status) {

        }
        public static double getOrdersAmountByState(Collection<Order> orders, OrderStatus status){
            return orders.stream().filter(o -> o.status() == status).mapToDouble(Order::amount).sum();
        }

        public static Collection<Order> getOrdersByState(Map<User, List<Order>> orders, OrderStatus status) {
            return orders.entrySet().stream().flatMap(x->x.getValue().stream())
                            .filter(o->o.status()==status).toList();
        }

        public static Map<UserStatus,Collection<User>> partitionUserByState(Collection<User> users) {
            return users.stream().collect(Collectors.groupingBy(User::status,
                    mapping(Function.identity(), toCollection(ArrayList::new))));
        }

        public static Map<String, User> getMaxHighlyPayedPerDepartment(Collection<User> users) {

            return users.stream().collect(Collectors.toMap(User::department, Function.identity(),
                                BinaryOperator.maxBy(Comparator.comparingDouble(User::salary))));

        }

        public static Collection<String> getDuplicateEmails(Collection<User> users) {

            return users.stream().collect(Collectors.groupingBy(User::email,counting()))
                                .entrySet().stream().filter(x->x.getValue()>1)
                                                .map(Map.Entry::getKey).toList();

        }






    }
}
