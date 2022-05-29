package at.ac.fhcampuswien.exception;

// https://www.baeldung.com/java-new-custom-exception || last visit: 28.05.2022 15:56

public class NewsApiException extends Exception {
    public NewsApiException(String errorMessage) {
        super(errorMessage);
    }
}
