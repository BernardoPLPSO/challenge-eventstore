package net.intelie.challenges;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class EventStoreTest {
    Set<Event> data = new HashSet<Event>();
    EventStoreImpl eventStore = new EventStoreImpl(data);
	
	public Set<Event> getTestData(){
		Set<Event> setEvents = new HashSet<Event>();
        for(int i = 0; i < 500; i++) {
        	int randInt = new Random().nextInt(3);
        	setEvents.add(new Event("Event-"+randInt, i));
        }
        return setEvents;
	}
	
	@Test
    public void testNoInsert() throws Exception {
        Event event = new Event("Event-1", 123L);
        
        assertTrue(eventStore.dataStructure.size() == 0);
        assertFalse(eventStore.dataStructure.contains(event));
    }
	
    @Test
    public void testSingleInsert() throws Exception {
        Event event = new Event("Event-1", 123L);
        Event event2 = new Event("Event-2", 123L);
        eventStore.insert(event);
        
        assertTrue(eventStore.dataStructure.size() == 1);
        assertTrue(eventStore.dataStructure.contains(event));
        assertFalse(eventStore.dataStructure.contains(event2));
    }
    
    @Test
    public void testMultipleInsert() throws Exception {
        Set<Event> testData = getTestData();
        for(Event event : testData) {
        	 eventStore.insert(event);
        }
        
        assertTrue(eventStore.dataStructure.size() == testData.size());
        for(Event event : testData) {
        	assertTrue(eventStore.dataStructure.contains(event));
        }
        Event falseEvent = new Event("Event-4", 123L);
        
        assertFalse(eventStore.dataStructure.contains(falseEvent));
    }
    
    @Test
    public void testQueryAllTimestamps() throws Exception {
        Set<Event> testData = getTestData();
        for(Event event : testData) {
        	 eventStore.insert(event);
        }
        Set<Event> projectedQueryResult = new HashSet<Event>();
        for(Event event: testData){
        	if(event.type().equals("Event-1") && event.timestamp() >= 0 && event.timestamp() < Long.MAX_VALUE) {
        		projectedQueryResult.add(event);
        	}
        }
        
        EventIterator eventIterator = eventStore.query("Event-1", 0, Long.MAX_VALUE);
        int count = 0;
        while(eventIterator.moveNext()) {
        	count++;
        	Event event = eventIterator.current();
        	assertTrue(projectedQueryResult.contains(event));
        }
        Event falseEvent = new Event("Event-2", 123L);
        assertFalse(projectedQueryResult.contains(falseEvent));
        assertTrue(count == projectedQueryResult.size());
    }
    
    @Test
    public void testQuerySomeTimestamps() throws Exception {
        Set<Event> testData = getTestData();
        for(Event event : testData) {
        	 eventStore.insert(event);
        }
        Set<Event> projectedQueryResult = new HashSet<Event>();
        for(Event event: testData){
        	if(event.type().equals("Event-1") && event.timestamp() >= 250 && event.timestamp() < 300) {
        		projectedQueryResult.add(event);
        	}
        }
        
        EventIterator eventIterator = eventStore.query("Event-1", 250, 300);
        int count = 0;
        while(eventIterator.moveNext()) {
        	count++;
        	Event event = eventIterator.current();
        	assertTrue(projectedQueryResult.contains(event));
        }
        Event falseEvent = new Event("Event-2", 123L);
        assertFalse(projectedQueryResult.contains(falseEvent));
        assertTrue(count == projectedQueryResult.size());
    }
    
    @Test
    public void testQueryAllEventsEmptyType() throws Exception {
        Set<Event> testData = getTestData();
        for(Event event : testData) {
        	 eventStore.insert(event);
        }
        
        EventIterator eventIterator = eventStore.query("", 0L, 0L);
        int count = 0;
        while(eventIterator.moveNext()) {
        	count++;
        	Event event = eventIterator.current();
        	assertTrue(testData.contains(event));
        }
        Event falseEvent = new Event("Event-2", 123L);
        assertFalse(testData.contains(falseEvent));
        assertTrue(count == testData.size());
    }
    
    @Test
    public void testQueryAllEventsNullType() throws Exception {
        Set<Event> testData = getTestData();
        for(Event event : testData) {
        	 eventStore.insert(event);
        }
        
        EventIterator eventIterator = eventStore.query(null, 0L, 0L);
        int count = 0;
        while(eventIterator.moveNext()) {
        	count++;
        	Event event = eventIterator.current();
        	assertTrue(testData.contains(event));
        }
        Event falseEvent = new Event("Event-2", 123L);
        assertFalse(testData.contains(falseEvent));
        assertTrue(count == testData.size());
    }
    
    @Test
    public void testRemoveAll() throws Exception {
        Set<Event> testData = getTestData();
        for(Event event : testData) {
        	 eventStore.insert(event);
        }
        Set<Event> projectedQueryResult = new HashSet<Event>();
        for(Event event: testData){
        	if(!event.type().equals("Event-1")) {
        		projectedQueryResult.add(event);
        	}
        }
        eventStore.removeAll("Event-1");
        EventIterator eventIterator = eventStore.query("", 0L, 0L);
        int count = 0;
        while(eventIterator.moveNext()) {
        	count++;
        	Event event = eventIterator.current();
        	assertTrue(projectedQueryResult.contains(event));
        }
        Event falseEvent = new Event("Event-1", 123L);
        assertFalse(projectedQueryResult.contains(falseEvent));
        assertTrue(count == projectedQueryResult.size());
    }
}