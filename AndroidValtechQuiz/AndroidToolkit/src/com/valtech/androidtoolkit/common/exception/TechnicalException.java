package com.valtech.androidtoolkit.common.exception;

public abstract class TechnicalException extends Exception
{
	private static final long serialVersionUID = 7164418879281647091L;

	public TechnicalException() {
		super();
	}

	public TechnicalException(Throwable pThrowable) {
		super(pThrowable);
	}

	public TechnicalException(String pMessage, Object... pArguments) {
		super(String.format(pMessage, pArguments));
	}
	
	public TechnicalException(Throwable pThrowable, String pMessage, Object... pArguments) {
		super(String.format(pMessage, pArguments), pThrowable);
	}
}
