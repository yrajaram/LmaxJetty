package com.herakles.lmax;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.herakles.lmax.event.MiddlewareMessage;
import com.herakles.lmax.handler.AnyExceptionHandler;
import com.herakles.lmax.handler.ParseAndProcessEventHandler;
import com.herakles.lmax.handler.JournalEventHandler;
import com.herakles.lmax.handler.CheckIfDuplicateEventHandler;
import com.herakles.lmax.handler.SaveDetailsEventHandler;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/*
 * Usage: http://localhost:8080/LmaxJetty/mom?id=7&mep=none
 * 
 * Examples:
 * http://www.wjblackburn.me/resources/LMAX-disruptor-example.html
 * 
 * http://www.abstractclass.org/tutorial/java/2015/05/20/tutorial-how-to-use-lmax-disruptor.html
 */
public class MomServlet extends HttpServlet {
	private static final long serialVersionUID = 563048602071847542L;
	private RingBuffer<MiddlewareMessage> ringBuffer;
	private com.lmax.disruptor.dsl.Disruptor<MiddlewareMessage> disruptor;
    private final ExecutorService EXECUTOR = Executors.newFixedThreadPool(4);

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.getWriter().println("<h1>LMax Disruptor MOM Servlet</h1>");
		response.getWriter().println("session=" + request.getSession(true).getId());
//------------------------------ do something useful
		String id = request.getParameter("id");
		String mep = request.getParameter("mep");
		if (id != null && !id.isEmpty()) {
			long sequence = ringBuffer.next();
			try {
				// Do some work with the event.
				MiddlewareMessage message = ringBuffer.get(sequence);

				message.setId(Long.parseLong(id));
				message.setType(mep);
			} finally {
				ringBuffer.publish(sequence);
			}
		} else {
			response.getWriter().println("Unique messageID and MEP are expected");
		}
		response.getWriter().flush();
		response.getWriter().println("Done");
	}

	@SuppressWarnings("unchecked")
	public void init() {
		/*
		disruptor = new Disruptor<MiddlewareMessage>(
				MiddlewareMessage.FACTORY, 1024, Executors.newCachedThreadPool());
				*/
		disruptor = new Disruptor<MiddlewareMessage>(
				MiddlewareMessage.FACTORY, 
                1024, 
                Executors.newFixedThreadPool(5),
                ProducerType.SINGLE,
                new YieldingWaitStrategy());

		EventHandler<MiddlewareMessage> journalMsg = new JournalEventHandler();
		EventHandler<MiddlewareMessage> duplicateMsgHandler = new CheckIfDuplicateEventHandler();
		EventHandler<MiddlewareMessage> parseAndProcess = new ParseAndProcessEventHandler();
		EventHandler<MiddlewareMessage> detailsSaver= new SaveDetailsEventHandler();
		ExceptionHandler<MiddlewareMessage> exceptionHandler = new AnyExceptionHandler();

		disruptor.handleEventsWith(journalMsg, duplicateMsgHandler).then(parseAndProcess).then(detailsSaver);
		disruptor.handleExceptionsFor(journalMsg).with(exceptionHandler);
		disruptor.handleExceptionsFor(duplicateMsgHandler).with(exceptionHandler);
		disruptor.handleExceptionsFor(parseAndProcess).with(exceptionHandler);
		disruptor.handleExceptionsFor(detailsSaver).with(exceptionHandler);
		this.ringBuffer = disruptor.start();
	}
	
    public void destroy() {
        try {
            disruptor.shutdown();
        } catch (Exception ignored) {}
        
        EXECUTOR.shutdownNow();
    }
}
