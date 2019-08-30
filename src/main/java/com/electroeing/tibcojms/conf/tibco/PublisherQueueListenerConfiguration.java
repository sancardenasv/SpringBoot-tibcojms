package com.electroeing.tibcojms.conf.tibco;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.connection.UserCredentialsConnectionFactoryAdapter;
import org.springframework.jms.listener.DefaultMessageListenerContainer;
import org.springframework.jms.listener.SessionAwareMessageListener;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.jndi.JndiTemplate;

@Configuration
//@ComponentScan(basePackageClasses = PasswordManagerConfiguration.class)
public class PublisherQueueListenerConfiguration extends AbstractQueueListenerConfiguration {
   @Value("${jms.tibco.initialcnf}")
   private String templatePropertyInitial;
   @Value("${jms.tibco.jndiProviderUrl}")
   private String templatePropertyProviderUrl;
   @Value("${jms.tibco.sslEnableVerifyHost}")
   private String sslEnableVerifyHost;
   @Value("${jms.tibco.connectionFactory}")
   private String jndiName;
   @Value("${jms.tibco.credentials.user}")
   private String user;
   @Value("${jms.tibco.queueName}")
   private String queueName;

   @Bean("tibcoPublisherJndiTemplate")
   JndiTemplate jndiTemplate() {
      return getJndiTemplate(templatePropertyInitial, templatePropertyProviderUrl, sslEnableVerifyHost);
   }

   @Bean("tibcoPublisherJndiFactoryObject")
   JndiObjectFactoryBean jndiObjectFactoryBean() {
      return getJndiObjectFactoryBean(jndiName, jndiTemplate());
   }

   @Primary
   @Bean("tibcoPublisherConnectionFactory")
   UserCredentialsConnectionFactoryAdapter connectionFactory(@Qualifier("tibcoPassword") String tibcoPassword) {
      return getUserCredentialsConnectionFactoryAdapter(user, tibcoPassword, jndiObjectFactoryBean());
   }

   @Bean("tibcoPublisherJmsContainer")
   DefaultMessageListenerContainer jsmContainer(
           @Qualifier("tibcoJmsListener") SessionAwareMessageListener<?> publisherJmsListener,
           @Qualifier("tibcoPublisherConnectionFactory") UserCredentialsConnectionFactoryAdapter connectionFactory) {
      return getDefaultMessageListenerContainer(queueName, connectionFactory, publisherJmsListener);
   }

   @Bean(name= "tibcoJmsListener")
   public TibcoJmsListener publisherJmsListener() {
      return new TibcoJmsListener();
   }

}
