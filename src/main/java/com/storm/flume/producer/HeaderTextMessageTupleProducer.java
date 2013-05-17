package com.storm.flume.producer;

import java.util.UUID;

import org.apache.flume.Event;

import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

import com.storm.flume.common.Constants;

/**
 * @author Ravikumar Visweswara
 */

@SuppressWarnings("serial")
public class HeaderTextMessageTupleProducer implements AvroTupleProducer{

	public Values toTuple(Event event) throws Exception {
		String msgID = event.getHeaders().get(Constants.MESSAGE_ID);
		
		//if MessageID header doesn't exists, set the MessageId
		if(null == msgID) {
			UUID randMsgID = UUID.randomUUID();
			msgID = randMsgID.toString();
			event.getHeaders().put(Constants.MESSAGE_ID, msgID);
		}
		String msg = new String(event.getBody());
		return new Values(msgID, event.getHeaders(),msg);
	}

	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields(Constants.MESSAGE_ID,Constants.HEADERS,Constants.MESSAGE));
	}

}
