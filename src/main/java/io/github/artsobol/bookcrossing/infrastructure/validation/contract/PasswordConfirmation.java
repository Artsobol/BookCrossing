package io.github.artsobol.bookcrossing.infrastructure.validation.contract;

public interface PasswordConfirmation {
    String password();

    String confirmPassword();
}
