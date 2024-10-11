package com.tfg.backend.model.exceptions;

import lombok.Getter;

@Getter
@SuppressWarnings("serial")
public class IncorrectLoginException extends Exception {

    private String userName;
    private String password;

    public IncorrectLoginException(String userName, String password) {

        this.userName = userName;
        this.password = password;

    }

}
