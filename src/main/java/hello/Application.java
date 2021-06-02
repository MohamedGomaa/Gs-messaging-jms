package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import javax.jms.ConnectionFactory;

@SpringBootApplication
@EnableJms
public class Application {
    @Bean
    public JmsListenerContainerFactory<?> myFactory(ConnectionFactory theConnectionFactory,
                                                    DefaultJmsListenerContainerFactoryConfigurer theConfigurer){

        DefaultJmsListenerContainerFactory theFactory = new DefaultJmsListenerContainerFactory();

        //providing all boot's default to this factory, including the message converter
        theConfigurer.configure(theFactory,theConnectionFactory);

        return theFactory;
    }

    //Serializing message content to Json using TextMessage
    @Bean
    public MessageConverter JacksonJmsMessageConverter(){

        MappingJackson2MessageConverter theConverter = new MappingJackson2MessageConverter();

        theConverter.setTargetType(MessageType.TEXT);
        theConverter.setTypeIdPropertyName("_type");

        return theConverter;
    }

    public static void main(String[] args){
        //Launch the application
        ConfigurableApplicationContext theContext = SpringApplication.run(Application.class, args);

        JmsTemplate theJmsTemplate = theContext.getBean(JmsTemplate.class);

        //Sending message to POJO
        System.out.println("Sending an email message: ");
        theJmsTemplate.convertAndSend("mailbox", new Email("gomaa14@hotmail.com", "Hello Mohamed!"));
    }
}
