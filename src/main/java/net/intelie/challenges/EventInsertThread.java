package net.intelie.challenges;

/**
 * Runnable class for inserting an event into the store
 *
 */
public class EventInsertThread implements Runnable{	
	EventStore eventStore;
	String eventType;
	long timestamp;
	
	/**
	 * Receives the store, the type of event and its timestamp, to insert them into the store
	 * @param eventStore
	 * @param eventType
	 * @param timestamp
	 */
	EventInsertThread(EventStore eventStore, String eventType, long timestamp){
		this.eventStore=eventStore;
		this.eventType=eventType;
		this.timestamp=timestamp;
	}
	
	/**
	 * Simply calls the insert method of the EventStore class, using the atributes of the class.
	 */
	@Override
	public void run() {
		eventStore.insert(new Event(eventType, timestamp));
		System.out.println("Inserted " + eventType + " - " + timestamp);
		System.out.println(Thread.currentThread()+ "finished");
	}

}
