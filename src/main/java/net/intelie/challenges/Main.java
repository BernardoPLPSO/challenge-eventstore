package net.intelie.challenges;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 
 * Main class that calls all relevant methods, used for preliminary testing
 *
 */
public class Main {
	/**
	 * Calls the other methods implemented in this class in order.
	 * First it populates the store with 1000 events, then it queries them randomly 10 times, 
	 * then it removes all the events, then it queries the store again, to show that there are no longer any events left
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String [] args) throws InterruptedException {
		EventStore eventStore = populateStore();
		testQuery(eventStore);
		testRemoveAll(eventStore);
		testQuery(eventStore);
	}
	
	/**
	 * Creates and populates an EventStore with mostly random events, that can be of 3 different types. 
	 * This implementation utilizes ExecutorService to create a thread pool (in this case, of 5 threads) to
	 * limit the ammount of threads that are created, so as to not create 1000 threads. Before the method returns
	 * the now populated EventStore, it will make the program wait for all the threads to finish their executions. 
	 * The threads utilize the EventInsertThread code to call the methods in the EventStore. 
	 * @return
	 * @throws InterruptedException
	 */
	public static EventStore populateStore() throws InterruptedException {
		Set<Event> dataStructure = new HashSet<Event>();
		EventStore eventStore = new EventStoreImpl(dataStructure);
		String eventTypes[] = new String[] {"Event-1", "Event-2", "Event-3"};
		ExecutorService pool = Executors.newFixedThreadPool(5);
        for(int i = 0; i <= 1000; i++){
        	int rnd = new Random().nextInt(eventTypes.length);
            Runnable r = new EventInsertThread(eventStore, eventTypes[rnd], System.currentTimeMillis());
            pool.execute(r);
        }
        pool.shutdown();
        pool.awaitTermination(5,TimeUnit.SECONDS);
		return eventStore;
	}
	
	/**
	 * Creates the threads to run queries. In this case, it makes 10 queries, 
	 * with the type of event being randomized for each query. It utilizes the same 
	 * ExecutorService logic as the populateStore method, with a pool of 5 threads
	 * @param eventStore
	 * @throws InterruptedException
	 */
	public static void testQuery(EventStore eventStore) throws InterruptedException {
		String eventTypes[] = new String[] {"Event-1", "Event-2", "Event-3"};
		ExecutorService pool = Executors.newFixedThreadPool(5);
		for(int i = 1; i <= 10; i++){
        	int rnd = new Random().nextInt(eventTypes.length);
            Runnable r = new EventQueryThread(eventStore, eventTypes[rnd], 0, Long.MAX_VALUE);
            pool.execute(r);
        }
        pool.shutdown();
        pool.awaitTermination(5,TimeUnit.SECONDS);
	}
	
	/**
	 * Creates the threads to run the removeAll method. In this case, it makes 3 removals, 
	 * one for each kind of event. It utilizes the same ExecutorService logic as the populateStore method, 
	 * with a pool of 5 threads
	 * @param eventStore
	 * @throws InterruptedException
	 */
	public static void testRemoveAll(EventStore eventStore) throws InterruptedException {
		String eventTypes[] = new String[] {"Event-1", "Event-2", "Event-3"};
		ExecutorService pool = Executors.newFixedThreadPool(5);
        for(int i = 0; i <= 2; i++){
            Runnable r = new EventRemoveAllThread(eventStore, eventTypes[i]);
            pool.execute(r);
        }
        pool.shutdown();
        pool.awaitTermination(5,TimeUnit.SECONDS);
	}
}
