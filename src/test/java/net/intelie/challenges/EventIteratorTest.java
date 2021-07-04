package net.intelie.challenges;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class EventIteratorTest {
    Set<Event> data = new HashSet<Event>();
    EventIteratorImpl eventIterator = new EventIteratorImpl(data);
    
	@Test
	public void testMoveNextEmptySet() {
		assertFalse(eventIterator.moveNext());
	}
	
	@Test
	public void testMoveNextSetOne() {
		data.add(new Event("Event-1", 123L));
		EventIterator eventIteratorResult = new EventIteratorImpl(data);
		assertTrue(eventIteratorResult.moveNext());
	}
	
	@Test
	public void testMoveNextSetMultiple() {
		for(int i = 0; i < 500; i++) {
			int randInt = new Random().nextInt(3);
			data.add(new Event("Event-"+randInt, i));
		}
		EventIterator eventIteratorResult = new EventIteratorImpl(data);
		assertTrue(eventIteratorResult.moveNext());
	}
	
	@Test
	public void testCurrentEmptySet() {
		assertThrows(IllegalStateException.class, () -> {eventIterator.current();});
	}
	
	@Test
	public void testCurrentSetOneNoMoveNext() {
		data.add(new Event("Event-1", 123L));
		EventIterator eventIteratorResult = new EventIteratorImpl(data);
		assertThrows(IllegalStateException.class, () -> {eventIteratorResult.current();});
	}
	
	@Test
	public void testCurrentSetOneWithMoveNext() {
		data.add(new Event("Event-1", 123L));
		EventIterator eventIteratorResult = new EventIteratorImpl(data);
		eventIteratorResult.moveNext();
		assertNotNull(eventIteratorResult.current());
	}
	
	@Test
	public void testCurrentSetMultipleNoMoveNext() {
		for(int i = 0; i < 500; i++) {
			int randInt = new Random().nextInt(3);
			data.add(new Event("Event-"+randInt, i));
		}
		EventIterator eventIteratorResult = new EventIteratorImpl(data);
		assertThrows(IllegalStateException.class, () -> {eventIteratorResult.current();});
	}
	
	@Test
	public void testCurrentSetMultipleWithMoveNext() {
		for(int i = 0; i < 500; i++) {
			int randInt = new Random().nextInt(3);
			data.add(new Event("Event-"+randInt, i));
		}
		EventIterator eventIteratorResult = new EventIteratorImpl(data);
		while(eventIteratorResult.moveNext()) {
			assertNotNull(eventIteratorResult.current());
		}
	}
	
	@Test
	public void testRemoveEmptySet() {
		assertThrows(IllegalStateException.class, () -> {eventIterator.remove();});
	}
	
	@Test
	public void testRemoveOneSetOneNoMoveNext() {
		data.add(new Event("Event-1", 123L));
		EventIterator eventIteratorResult = new EventIteratorImpl(data);
		assertThrows(IllegalStateException.class, () -> {eventIteratorResult.remove();});
	}
	
	@Test
	public void testRemoveOneSetOneWithMoveNext() {
		data.add(new Event("Event-1", 123L));
		EventIteratorImpl eventIteratorResult = new EventIteratorImpl(data);
		while(eventIteratorResult.moveNext()) {
			eventIteratorResult.current();
			eventIteratorResult.remove();
		}
		assertTrue(eventIteratorResult.dataStructure.isEmpty());
	}
	
	@Test
	public void testRemoveOneSetTwoWithMoveNext() {
		data.add(new Event("Event-1", 123L));
		data.add(new Event("Event-2", 123L));
		EventIteratorImpl eventIteratorResult = new EventIteratorImpl(data);
		while(eventIteratorResult.moveNext()) {
			eventIteratorResult.current();
			eventIteratorResult.remove();
			break;
		}
		assertTrue(eventIteratorResult.dataStructure.size() == 1);
	}
}