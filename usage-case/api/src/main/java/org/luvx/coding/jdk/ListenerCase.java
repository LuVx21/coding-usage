package org.luvx.coding.jdk;

import lombok.Setter;

import java.util.EventListener;
import java.util.EventObject;

public class ListenerCase {
    /**
     * 事件源
     */
    @Setter
    private static class EventSource {
        private Listener listener;

        private Event click() {
            Event event = new Event(this);
            listener.listen(event);
            return event;
        }

        public void registerLister(Listener listener) {
            this.setListener(listener);
        }
    }

    /**
     * 事件
     */
    public static class Event extends EventObject {
        Event(Object source) {
            super(source);
        }
    }

    /**
     * 监听器
     */
    public static class Listener implements EventListener {
        private void listen(EventObject obj) {
            EventSource source = (EventSource) obj.getSource();
            System.out.println("监听到事件源:" + source);
        }
    }

    public static void main(String[] args) {
        EventSource source = new EventSource();
        source.registerLister(new Listener());
        source.click();
    }
}
