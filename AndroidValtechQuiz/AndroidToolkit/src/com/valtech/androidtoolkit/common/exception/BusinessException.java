package com.valtech.androidtoolkit.common.exception;

public abstract class BusinessException extends Exception
{
	private static final long serialVersionUID = 7164418879281647091L;

	public BusinessException() {
		super();
	}

	public BusinessException(Throwable pThrowable) {
		super(pThrowable);
	}

	public BusinessException(String pMessage, Object... pArguments) {
		super(String.format(pMessage, pArguments));
	}
	
	public BusinessException(Throwable pThrowable, String pMessage, Object... pArguments) {
		super(String.format(pMessage, pArguments), pThrowable);
	}
}
