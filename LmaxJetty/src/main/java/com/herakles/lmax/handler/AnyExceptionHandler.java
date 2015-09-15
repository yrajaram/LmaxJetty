package com.herakles.lmax.handler;

import com.herakles.lmax.event.MiddlewareMessage;
import com.lmax.disruptor.ExceptionHandler;

public class AnyExceptionHandler implements ExceptionHandler<MiddlewareMessage> {

	public void handleEventException(Throwable ex, long sequence,	MiddlewareMessage msg) {
		System.out.println("Exception received - " + ex + " when processing sequence:" + sequence	+ " msg:" + msg);
	}

	public void handleOnShutdownException(Throwable ex) {
		System.out.println("Unexpected exception on shutdown -" + ex);
	}

	public void handleOnStartException(Throwable ex) {
		System.out.println("Unexpected exception on Start -" + ex);

	}

}
