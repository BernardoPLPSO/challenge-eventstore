package net.intelie.challenges;

/**
 * Runnable class for querying events from the store
 *
 */
public class EventQueryThread implements Runnable{	
	EventStore eventStore;
	String eventType;
	long startTimestamp;
	long endTimestamp;
	
	/**
	 * Receives the store, the event type, the start timestamp and end timestamp that are used for querying events
	 * @param eventStore
	 * @param eventType
	 * @param startTimestamp
	 * @param endTimestamp
	 */
	EventQueryThread(EventStore eventStore, String eventType, long startTimestamp, long endTimestamp){
		this.eventStore=eventStore;
		this.eventType=eventType;
		this.startTimestamp=startTimestamp;
		this.endTimestamp=endTimestamp;
		
	}
	
	/**
	 * Calls the query method from the EventStore class, and uses it in a try-with-resources statement, prints the result on console.
	 * At the end of the try-with-resources, since the EventIterator extends AutoCloseable, the close() method from the implementation is automatically called
	 */
	@Override
	public void run() {
		try(EventIterator eventIterator = eventStore.query(eventType, startTimestamp, endTimestamp)){
			while(eventIterator.moveNext()) {
				Event event = eventIterator.current();
				System.out.println(Thread.currentThread() + ": " + event.type() + " - " + event.timestamp());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		System.out.println(Thread.currentThread() + "ending");
	}

}
