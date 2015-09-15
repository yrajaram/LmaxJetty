package com.herakles.lmax.handler;

import com.herakles.lmax.event.MiddlewareMessage;
import com.lmax.disruptor.EventHandler;

public class ParseAndProcessEventHandler implements EventHandler<MiddlewareMessage> {
	public void onEvent(MiddlewareMessage event, long sequence, boolean endOfBatch)
			throws Exception {
		// generate custom dsl code and execute it
		System.out.println("Generate code and execute it");
		if (event.getId()==3) throw new Exception("Code Generation 3");
	}
}