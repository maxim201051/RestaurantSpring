package ua.training.restaurant.entity;

public interface RegexContainer {
    String NAME_REGEX = "^[A-Z][a-z]{3,20}$";
    String NAME_REGEX_UA = "^[А-ЩЬЮЯҐІЇЄ][а-щьюяґіїє']{3,20}$";
    String USERNAME_REGEX = "^[a-z0-9_-]{3,15}$";
}
