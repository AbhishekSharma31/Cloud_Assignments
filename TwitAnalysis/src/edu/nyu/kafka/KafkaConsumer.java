/*
 
 Cloud Computing
 Assignment 1
 
 Created by Abhishek Sharma on 10/25/15.
 Copyright (c) 2015 Abhishek. All rights reserved.
 
*/
package edu.nyu.kafka;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import edu.nyu.alchemy.Alchemy;
import kafka.consumer.Consumer;
import kafka.consumer.ConsumerConfig;
import kafka.consumer.ConsumerIterator;
import kafka.consumer.KafkaStream;
import kafka.javaapi.consumer.ConsumerConnector;

/**
 * Created by user on 8/4/14.
 */
public class KafkaConsumer extends Thread {
	final static String clientId = "SimpleConsumerDemoClient";
	final String topic;
	final Map<String, Integer> topicCountMap;
	final Map<String, List<KafkaStream<byte[], byte[]>>> consumerMap;
	final ConsumerConnector consumerConnector;
	final KafkaStream<byte[], byte[]> stream;
	ByteArrayInputStream bis;
	ObjectInputStream ois;
	Alchemy alchemy;

	public KafkaConsumer(String topic) {
		this.topic = topic;
		Properties properties = new Properties();
		properties.put("zookeeper.connect", "52.3.108.199");
		properties.put("advertised.host.name", "52.3.108.199");
		//properties.put("zookeeper.connect", "localhost:2181");

		properties.put("group.id", "test-group");
		ConsumerConfig consumerConfig = new ConsumerConfig(properties);
		consumerConnector = Consumer.createJavaConsumerConnector(consumerConfig);
		topicCountMap = new HashMap<String, Integer>();
		topicCountMap.put(this.topic, new Integer(1));
		consumerMap = consumerConnector.createMessageStreams(topicCountMap);
		stream = consumerMap.get(this.topic).get(0);
		alchemy = new Alchemy();
	}

	@Override
	public void run() {
		ConsumerIterator<byte[], byte[]> it = stream.iterator();
		while (it.hasNext()) {
			alchemy.fetchSentiments(new String(it.next().message()));
		}
	}

	public static void main(String[] argv) throws UnsupportedEncodingException {
		KafkaConsumer helloKafkaConsumer = new KafkaConsumer("KafkaTest");
		helloKafkaConsumer.start();
	}
}