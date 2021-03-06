package net.stawrul.services.exceptions;

/**
 * Wyjątek sygnalizujący duplikat zamówionego towaru.
 *
 * Wystąpienie wyjątku z hierarchii RuntimeException w warstwie biznesowej
 * powoduje wycofanie transakcji (rollback).
 */
public class DuplicateException extends RuntimeException {
}
