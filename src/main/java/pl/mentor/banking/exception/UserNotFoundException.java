package pl.mentor.banking.exception;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long userId) {
        super("Bank nie posiada użytkownika o ID: " + userId);
    }
}
