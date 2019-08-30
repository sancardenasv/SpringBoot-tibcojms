package com.electroeing.tibcojms.conf.tibco;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.listener.SessionAwareMessageListener;

import javax.jms.Message;
import javax.jms.Session;

public class TibcoJmsListener implements SessionAwareMessageListener<Message> {
    private static final Logger logger = LoggerFactory.getLogger(TibcoJmsListener.class);

    @Override
    public void onMessage(final Message message, final Session session) {
        logger.info("Message received: {}", message);
    }
}
