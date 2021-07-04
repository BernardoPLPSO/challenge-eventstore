package net.intelie.challenges;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * An implementation of an event store.
 * <p>
 * In this instance, events are stored in a SynchronizedSet, 
 * this is used because Sets in general are faster than lists, and since the events are unique 
 * and do not require ordering for our purposes, we use the Synchronized version of sets because we need to ensure
 * that our collection is thread safe.
 */
public class EventStoreImpl implements EventStore{
	
	Set<Event> dataStructure;
	
	/**
	 * Constructor receives an {@link Event} set and transforms it into a synchronized set 
	 * @param dataStructure
	 */
	EventStoreImpl(Set<Event> dataStructure){
		this.dataStructure = Collections.synchronizedSet(dataStructure);		
	}
	
	/**
     * Stores an event
     *
     * @param event
     */
	@Override
	public synchronized void insert(Event event) {
		this.dataStructure.add(event);
	}
	
	/**
     * Removes all events of specific type.
     * The method is synchronized to keep thread safety in mind, since otherwise multiple threads could try
     * to delete the same events at the same time. 
     *
     * @param type
     */
	@Override
	public synchronized void removeAll(String type) {
		//We initialize the EventIterator using the dataStructure that was used to create this instance  
		EventIterator eventIterator = new EventIteratorImpl(this.dataStructure);
		//We iterate over the dataStructure, using the aforementioned eventIterator, 
		//and remove from it any events that match the type that was passed as parameter 
		while(eventIterator.moveNext()) {
			Event event = eventIterator.current();
			if(event.type().equals(type)){
				eventIterator.remove();
			}
		}
	}
	
	/**
     * Retrieves an iterator for events based on their type and timestamp.
     * This method is not synchronized, because it does not make any changes to any of the events,
     * therefore it is not unsafe for multiple threads to call it in parallel.
     * 
     * @param type      The type we are querying for.
     * @param startTime Start timestamp (inclusive).
     * @param endTime   End timestamp (exclusive).
     * @return An iterator where all its events have same type as
     * {@param type} and timestamp between {@param startTime}
     * (inclusive) and {@param endTime} (exclusive).
     */
	@Override
	public EventIterator query(String type, long startTime, long endTime) {
		//This first if is just an aditional case, if the user wants to query all of the events inside the store
		//it was mostly used to test the removeAll function, so I didn't have to make two queries for the rest 
		//of the events that weren't removed, basically, if you pass all the default values for the types of params in this method
		//it will return all the events.
		if((type==null || type.isEmpty()) && startTime == 0L && endTime == 0L) {
			return new EventIteratorImpl(this.dataStructure);
		}
		
		//This is the actual query that was asked, first we instantiate a new set, that's gonna be empty
		//after that, we get an EventIterator that contains all the events, after that, we iterate over it.
		//Whenever an event fits the conditions (same type, timestamp between the startTime (inclusive) and the endTime(exclusive)
		//it is added to that set (so that it only contains the events that belong in the query),
		//when the iteration is done, it returns a new EventIterator thats initialized with that set.
		Set<Event> resultSet = Collections.synchronizedSet(new HashSet<Event>());
		EventIterator eventIterator = new EventIteratorImpl(this.dataStructure);
		while(eventIterator.moveNext()) {
			Event event = eventIterator.current();
			if(event.type().equals(type) && event.timestamp() >= startTime && event.timestamp() < endTime) {
				resultSet.add(event);
			}
		}
		return new EventIteratorImpl(resultSet);
	}

}
