package net.intelie.challenges;

import java.util.Set;
import java.util.Collections;
import java.util.Iterator;

/**
 * An implementation of EventIteratorImpl, using a SynchronizedSet as the collection.
 * 
 */
public class EventIteratorImpl implements EventIterator{
	Set<Event> dataStructure;
	Iterator<Event> iterator;
	boolean moveNext;
	
	
	/**
	 * Constructor receives an {@link Event} set, transforms it into a synchronized set and uses this set to initialize the iterator. 
	 * @param dataStructure
	 */
	EventIteratorImpl(Set<Event> dataStructure) {
		this.dataStructure = Collections.synchronizedSet(dataStructure);
		this.iterator = dataStructure.iterator();
	}
	
	/**
	 * Sets the {@link moveNext} according to the iterator created in the constructor
	 */
	@Override
	public boolean moveNext() {
		this.moveNext = iterator.hasNext();
		return moveNext;
	}
	
	/**
	 * Returns the current element of the iterator created in the constructor
	 * 
	 * @return the event itself
	 * @throws IllegalStateException if {@link #moveNext} was never called
     *                               or its last result was {@code false}.
	 */
	@Override
	public Event current(){
		if(moveNext) {
			Event event = iterator.next();
			return event;
		} else {
			throw new IllegalStateException();
		}
	}
	
	/**
     * Remove current event from its store.
     *
     * @throws IllegalStateException if {@link #moveNext} was never called
     *                               or its last result was {@code false}.
     */
	@Override
	public void remove(){
		if(moveNext) {
			iterator.remove();
		} else {
			throw new IllegalStateException();
		}
	}
	
	/**
	 * Implementation of the AutoCloseable method, clears the set of its contents. Is called automatically whenever this iterator is used in a try-with-resources context 
	 */
	@Override
	public void close() throws Exception {
		dataStructure.clear();
		//System.out.println("Closing resource");
	}
}
