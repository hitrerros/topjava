package ru.javawebinar.topjava.util;


import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 500);
        getFilteredWithExceededStream(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 500);

        //        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        List<UserMealWithExceed> result = new ArrayList<>();
        Map<LocalDate, Integer> tempMap = new HashMap<>();


        for (UserMeal meal : mealList) {
            LocalDate tmpDate = meal.getDateTime().toLocalDate();
            LocalTime tmpTime = meal.getDateTime().toLocalTime();

            if (tmpTime.isAfter(startTime) && tmpTime.isBefore(endTime))
                tempMap.merge(
                        tmpDate,
                        meal.getCalories(),
                        (x, y) -> x + y);
        }


        mealList.forEach(k -> {
            result.add(new UserMealWithExceed(k.getDateTime(),
                    k.getDescription(),
                    k.getCalories(),
                    tempMap.get(k.getDateTime().toLocalDate()) >= caloriesPerDay));
        });

        return result;

    }

    public static List<UserMealWithExceed> getFilteredWithExceededStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {


        List<UserMealWithExceed> result = new ArrayList<>();
        final Map<LocalDate, Integer> tempMap = mealList.stream().filter(o -> o.getDateTime().toLocalTime().isAfter(startTime))
                .filter(o -> o.getDateTime().toLocalTime().isBefore(endTime))
                .collect(Collectors.toMap(o -> o.getDateTime().toLocalDate(), UserMeal::getCalories, (x, y) -> x + y));

        mealList.forEach(k -> {
            result.add(new UserMealWithExceed(k.getDateTime(),
                    k.getDescription(),
                    k.getCalories(),
                    tempMap.get(k.getDateTime().toLocalDate()) >= caloriesPerDay));
        });



        return result;
    }
}