package com.valtech.androidtoolkit.common.exception;

import com.google.common.base.Joiner;

/**
 * Indicates a case that should never happen and which indicates a programming or configuration
 * error (e.g. a reflection call which fails), a default case that should never happen in a switch,
 * etc.
 */
public class InternalException extends RuntimeException
{
    private static final long serialVersionUID = -4615749565432900659L;


    private InternalException(String pMessage, Object... pArguments) {
        super(String.format(pMessage, pArguments));
    }

    private InternalException(Throwable pThrowable, String pMessage, Object... pArguments) {
        super(String.format(pMessage, pArguments), pThrowable);
    }

    /**
     * Indicates that parameter should be an instance of pExpected but was an instance of pGiven.
     * @param pElementName Name of the variable or object that was incorrect.
     * @param pElement Object that is incorrect. Null value accepted.
     * @param pExpected Class that pElement should be an instance of. Null value accepted (but
     *            should never be).
     */
    public static InternalException hasWrongClass(String pElementName, Object pElement, Class<?> pExpected) {
        String expected = (pExpected != null) ? pExpected.getSimpleName() : "null";
        String given = (pElement != null) ? pElement.getClass().getSimpleName() : "null";
        return new InternalException(String.format("%1$s must be of type %2$s (and not %3$s)", pElementName, expected, given));
    }

    public static InternalException missingParameters(int pCountExpected, int pCountGiven) {
        return new InternalException(String.format("%1$s parameters provided instead of ", pCountGiven, pCountExpected));
    }

    public static InternalException invalidParameters(String pMessage, Object... pArguments) {
        return new InternalException(String.format(pMessage, pArguments));
    }

    public static InternalException isNull() {
        return new InternalException("Parameter is null");
    }

    public static InternalException oneIsNull(Object... pParameters) {
        StringBuilder message = new StringBuilder("A parameter in [");
        Joiner.on(" ,").appendTo(message, pParameters);
        message.append("] is null");
        return new InternalException(message.toString());
    }

    /**
     * Use this method for default switch or if/else case that should never occur.
     */
    public static InternalException illegalCase() {
        return new InternalException("Illegal case");
    }

    /**
     * Use this method when configuration is invalid.
     */
    public static InternalException invalidConfiguration(String pMessage, Object... pArguments) {
        return new InternalException(pMessage, pArguments);
    }

    /**
     * Use this method when configuration is invalid.
     */
    public static InternalException invalidConfiguration(Throwable pThrowable, String pMessage, Object... pArguments) {
        return new InternalException(pThrowable, pMessage, pArguments);
    }
}
