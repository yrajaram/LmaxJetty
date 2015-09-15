package com.herakles.lmax.event;

import com.lmax.disruptor.EventFactory;

public class MiddlewareMessage{
	private String type;
	private long id;

	public static final EventFactory<MiddlewareMessage> FACTORY =  new EventFactory<MiddlewareMessage>() {
        public MiddlewareMessage newInstance() {
            return new MiddlewareMessage();
        }
    };

	public void setId(long parseLong) {
		id= parseLong;
		System.out.println("id="+id);		
	}

	public void setType(String mep) {
		type = mep;
		System.out.println("type="+type);		
	}

	public String toString() {
		return String.format("MiddlewareMessage [id=%d, type=%s]", id, type);
	}

	public long getId() {
		return id;
	}

	public String getType() {
		return type;
	}
	
}