/*
 
 Cloud Computing
 Assignment 1
 
 Created by Abhishek Sharma on 10/22/15.
 Copyright (c) 2015 Abhishek. All rights reserved.
 
*/
package edu.nyu.main;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import edu.nyu.kafka.KafkaConsumer;
import edu.nyu.tweets.TweetsFetcher;
import twitter4j.TwitterException;

public class Main 
{
	public static void main(String args[]) throws TwitterException
	{
		ExecutorService executor = Executors.newFixedThreadPool(1);
		executor.submit(new TweetsFetcher());
		KafkaConsumer kafkaConsumer = new KafkaConsumer("tweets");
		kafkaConsumer.start();
	}
}
