package org.prd.ecommerce.controller.exceptions.services;

import org.prd.ecommerce.config.util.enums.StatusType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)//409
public class DuplicateDataException extends RuntimeException{

    private String field;
    private String value;

    private StatusType statusType;

    public DuplicateDataException(String field, String value, StatusType statusType) {
        super(String.format("%s '%s' already exits in database",field,value));
        this.field = field;
        this.value = value;
        this.statusType = statusType;
    }
    public StatusType getStatusType() {
        return statusType;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }
}
