/*
 
 Cloud Computing
 Assignment 1
 
 Created by Abhishek Sharma on 10/22/15.
 Copyright (c) 2015 Abhishek. All rights reserved.
 
*/
package edu.nyu.kafka;

import java.util.Properties;

import edu.nyu.tweets.TweetsData;
import kafka.producer.KeyedMessage;
import kafka.producer.ProducerConfig;

public class KafkaProducer 
{
	public final String topic;
	public final ProducerConfig producerConfig;
	public final Properties properties;
	public kafka.javaapi.producer.Producer<String,String> producer;
	
	public KafkaProducer(String topic)
	{
		this.topic = topic;
		properties = new Properties();
		properties.put("metadata.broker.list","52.3.108.199");
		properties.put("advertised.listeners","52.3.108.199");
		//properties.put("metadata.broker.list","localhost:9092");
	    properties.put("serializer.class","kafka.serializer.StringEncoder");
		this.producerConfig = new ProducerConfig(properties);
		producer = new kafka.javaapi.producer.Producer<String, String>(producerConfig);
	}
	
	public void sendMessage(String msg)
	{
		KeyedMessage<String, String> message =new KeyedMessage<String, String>(this.topic, msg);
	    producer.send(message);
	}
	
	
	public void close()
	{
		producer.close();
	}
	public static void main(String args[])
	{
		TweetsData td = new TweetsData();
		td.setTweet("happy birthday to you");
	    KafkaProducer kafkaProducer = new KafkaProducer("KafkaTest");
	    kafkaProducer.sendMessage(td.getTweet());
	}
}
