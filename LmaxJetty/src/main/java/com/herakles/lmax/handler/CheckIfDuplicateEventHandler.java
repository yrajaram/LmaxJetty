package com.herakles.lmax.handler;

import com.herakles.lmax.event.MiddlewareMessage;
import com.lmax.disruptor.EventHandler;

public class CheckIfDuplicateEventHandler implements EventHandler<MiddlewareMessage> {
	public void onEvent(MiddlewareMessage event, long sequence, boolean endOfBatch)
			throws Exception {
		//load details from db and process
		System.out.println("Loading details from DB");
		if (event.getId()==1) throw new Error("Load 1");
	}
}