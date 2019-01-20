package com.kretov.accountmanagement.response;

import java.util.Collections;
import java.util.List;

import static com.kretov.accountmanagement.response.Status.ERROR;

public class Response<T> {
    private Status status;
    private String description;
    private List<T> result;

    public Response(Status status, String description, List<T> result) {
        this.status = status;
        this.description = description;
        this.result = result;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public static <T> Response<T> illegalFormatResponse() {
        return new Response<>(ERROR, "Illegal format of input data. Please, use number.", Collections.emptyList());
    }
}
