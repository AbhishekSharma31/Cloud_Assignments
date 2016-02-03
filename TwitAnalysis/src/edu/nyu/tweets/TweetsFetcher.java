/*
 
 Cloud Computing
 Assignment 1
 
 Created by Abhishek Sharma on 10/25/15.
 Copyright (c) 2015 Abhishek. All rights reserved.
 
*/
package edu.nyu.tweets;

import twitter4j.TwitterException;


public class TweetsFetcher implements Runnable
{
	final TweetGet tweetGet;
	
	public TweetsFetcher()
	{
		tweetGet = new TweetGet();
	}

	public void run()
	{
		try 
		{
			tweetGet.streamTweets();
		} 
		catch (TwitterException e) 
		{
			e.printStackTrace();
		}
	}
}
