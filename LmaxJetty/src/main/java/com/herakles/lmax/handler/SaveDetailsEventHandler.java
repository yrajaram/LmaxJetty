package com.herakles.lmax.handler;

import com.herakles.lmax.event.MiddlewareMessage;
import com.lmax.disruptor.EventHandler;

public class SaveDetailsEventHandler implements EventHandler<MiddlewareMessage> {
	public void onEvent(MiddlewareMessage event, long sequence, boolean endOfBatch)
			throws Exception {
		// save the details to db
		System.out.println("Save to DB");
		if (event.getId()==4) throw new Exception("Save 4");
	}
}