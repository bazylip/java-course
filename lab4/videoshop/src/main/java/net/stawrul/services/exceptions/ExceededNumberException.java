package net.stawrul.services.exceptions;

/**
 * Wyjątek sygnalizujący zamówienie filmów w ilości przekraczających dostępny stan.
 *
 * Wystąpienie wyjątku z hierarchii RuntimeException w warstwie biznesowej
 * powoduje wycofanie transakcji (rollback).
 */
public class ExceededNumberException extends RuntimeException {
}
