package com.herakles.lmax.handler;

import com.herakles.lmax.event.MiddlewareMessage;
import com.lmax.disruptor.EventHandler;

public class JournalEventHandler implements EventHandler<MiddlewareMessage> {
	public void onEvent(MiddlewareMessage event, long sequence, boolean endOfBatch)
			throws Exception {
		// find the template of this type
		System.out.println("Finding right template");
		if (event.getId()==2) throw new Exception("Find 2");
	}
}