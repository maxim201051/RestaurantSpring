package ua.training.restaurant.controller;

import ua.training.restaurant.entity.User;

public class UtilityController {
    public static boolean checkUserFieldsWithRegex(User user) {
        return user.getNameEN().matches(RegexContainer.NAME_REGEX) && user.getNameUA().matches(RegexContainer.NAME_REGEX_UA)
                && user.getUsername().matches(RegexContainer.USERNAME_REGEX);
    }
    public static boolean checkFundsToAdd(Long funds) {
        return funds > 0 && funds <= 100000;
    }
}