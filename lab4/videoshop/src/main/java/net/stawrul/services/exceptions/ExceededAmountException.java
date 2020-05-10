package net.stawrul.services.exceptions;

/**
 * Wyjątek sygnalizujący nadmiar zamówionych filmów.
 *
 * Wystąpienie wyjątku z hierarchii RuntimeException w warstwie biznesowej
 * powoduje wycofanie transakcji (rollback).
 */
public class ExceededAmountException extends RuntimeException {
}
