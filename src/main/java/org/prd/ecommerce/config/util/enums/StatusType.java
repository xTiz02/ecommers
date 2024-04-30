package org.prd.ecommerce.config.util.enums;

public enum StatusType {
    ERROR, //cuando hay un error
    ACCOUNT_DISABLED, //cuando la cuenta esta deshabilitada
    BAD_CREDENTIALS, //cuando las credenciales son incorrectas
    ACCOUNT_LOCKED, //cuando la cuenta esta bloqueada
    ACCOUNT_EXPIRED, //cuando la cuenta ha expirado
    CREDENTIALS_EXPIRED, //cuando las credenciales han expirado
    DUPLICATE_EMAIL, //cuando el email ya existe
    DUPLICATE_NICKNAME, //cuando el nickname ya existe
    DUPLICATE_DATA, //cuando los datos de la request ya existen
    DATA_EMPTY, //cuando los datos de la respuesta estan vacios
    DATA_OK, //cuando los datos de la respuesta son correctos(getall, find)
    INTERNAL_ERROR,//cualquier error que el front no pueda manejar
    REQUEST_DATA_INVALID,//cuando los datos de la request no son validos
    OK //cuando todo esta bien
}
