package com.electroeing.tibcojms.conf.tibco;

import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.jndi.JndiTemplate;

import javax.jms.ConnectionFactory;
import java.util.Properties;

public abstract class AbstractQueueListenerConfiguration {
    protected JndiTemplate getJndiTemplate(String initial, String providerUrl, String sslEnableVerifyHost) {
        Properties prop = new Properties();
        prop.setProperty("java.naming.factory.initial", initial);
        prop.setProperty("java.naming.provider.url", providerUrl);
        prop.setProperty("com.tibco.tibjms.naming.ssl_enable_verify_host", sslEnableVerifyHost);
        prop.setProperty("com.tibco.tibjms.naming.security_protocol", "ssl");

        return new JndiTemplate(prop);
    }

    protected UserCredentialsConnectionFactoryAdapter getUserCredentialsConnectionFactoryAdapter(
            String username,String password, JndiObjectFactoryBean jndiObjectFactoryBean) {
        UserCredentialsConnectionFactoryAdapter connection = new UserCredentialsConnectionFactoryAdapter();
        connection.setTargetConnectionFactory((ConnectionFactory) jndiObjectFactoryBean.getObject());
        connection.setUsername(username);
        connection.setPassword(password);

        return connection;
    }

    protected JndiObjectFactoryBean getJndiObjectFactoryBean(String jndiName, JndiTemplate jndiTemplate) {
        JndiObjectFactoryBean factory = new JndiObjectFactoryBean();
        factory.setJndiTemplate(jndiTemplate);
        factory.setJndiName(jndiName);
        factory.setLookupOnStartup(true);
        factory.setCache(true);
        factory.setProxyInterface(ConnectionFactory.class);

        return factory;
    }

    protected DefaultMessageListenerContainer getDefaultMessageListenerContainer(
            String destinationName, ConnectionFactory connectionFactory, SessionAwareMessageListener<?> messageListener) {
        DefaultMessageListenerContainer container = new DefaultMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setDestinationName(destinationName);
        container.setMessageListener(messageListener);
        container.setConcurrency("1-1");
        container.setSessionTransacted(true);
        container.setSessionAcknowledgeMode(2);
        container.setSessionAcknowledgeModeName("CLIENT_ACKNOWLEDGE");

        return container;
    }
}
