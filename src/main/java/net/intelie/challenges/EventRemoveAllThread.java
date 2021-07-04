package net.intelie.challenges;

/**
 * Runnable class for removing events of a certain type from the store
 *
 */
public class EventRemoveAllThread implements Runnable{	
	Thread thread;
	EventStore eventStore;
	String eventType;
	
	/**
	 * Receives the store and event type to remove
	 * @param eventStore
	 * @param eventType
	 */
	EventRemoveAllThread(EventStore eventStore, String eventType){
		this.eventStore=eventStore;
		this.eventType=eventType;
	}
	
	/**
	 * Simply calls the removeAll method from the EventStore class and prints on console what type was removed
	 */
	@Override
	public void run() {
		eventStore.removeAll(eventType);
		System.out.println(Thread.currentThread() + " - Removed all " + eventType + "'s - ");
		System.out.println(Thread.currentThread()+ "finished");
	}

}
