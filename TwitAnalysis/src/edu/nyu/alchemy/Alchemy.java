/*
 
 Cloud Computing
 Assignment 1
 
 Created by Abhishek Sharma on 10/22/15.
 Copyright (c) 2015 Abhishek. All rights reserved.
 
*/
package edu.nyu.alchemy;

import java.util.Iterator;
import java.util.concurrent.LinkedBlockingQueue;

import edu.nyu.tweets.TweetsData;
import com.google.gson.Gson;
import com.likethecolor.alchemy.api.Client;
import com.likethecolor.alchemy.api.call.AbstractCall;
import com.likethecolor.alchemy.api.call.SentimentCall;
import com.likethecolor.alchemy.api.call.type.CallTypeText;
import com.likethecolor.alchemy.api.entity.Response;
import com.likethecolor.alchemy.api.entity.SentimentAlchemyEntity;

public class Alchemy {
	final Gson gson;
	public static LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();
	public static double averageScore = 0.0;
	public static int count = 0;

	public Alchemy() {
		this.gson = new Gson();
	}

	public void fetchSentiments(String text) {
		String result = null;
		// Put Alchemy API key Here
		//final String apiKey = "***********************************";//
        
		final Client client = new Client(apiKey);
		System.out.println(text);
		TweetsData td = gson.fromJson(text, TweetsData.class);
		try {

			final AbstractCall<SentimentAlchemyEntity> sentimentCall = new SentimentCall(
					new CallTypeText(td.getTweet()));
			final Response<SentimentAlchemyEntity> sentimentResponse = client.call(sentimentCall);

			SentimentAlchemyEntity entity;
			final Iterator<SentimentAlchemyEntity> iter = sentimentResponse.iterator();
			while (iter.hasNext()) {
				entity = iter.next();

				result = entity.getType().toString();
				td.setSentiment(result);

				averageScore = (averageScore * count + entity.getScore());
				count++;
				averageScore = averageScore / count;
				td.setAverageScore(averageScore + "");
				queue.add(gson.toJson(td));
			}
		} catch (Exception e) {
			// e.printStackTrace();
			td.setAverageScore(averageScore + "");
			td.setSentiment("LIMITEXCEEDED");
			queue.add(gson.toJson(td));
		}
	}

	public static void main(String args[]) {
		// Alchemy a = new Alchemy();
	}
}
