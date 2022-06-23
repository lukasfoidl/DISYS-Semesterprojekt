package com.example.invoicegenerator.communication;

import com.example.invoicegenerator.store.CustomerDto;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;

import javax.jms.*;

public class Producer {
    public static void send(CustomerDto dto, String queueName, String brokerUrl) {
        // taken from: https://activemq.apache.org/hello-world

        try {
            ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory(brokerUrl);

            ActiveMQPrefetchPolicy policy = new ActiveMQPrefetchPolicy();
            policy.setAll(0);
            factory.setPrefetchPolicy(policy);

            Connection connection = factory.createConnection();
            connection.start();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue(queueName);

            MessageProducer producer = session.createProducer(destination);
            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            //producer.send(session.createTextMessage(text));

            ObjectMessage objectMessage = session.createObjectMessage();
            objectMessage.setObject(dto);
            producer.send(objectMessage);

            producer.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}

