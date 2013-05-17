package com.storm.flume.producer;

import java.io.Serializable;

import org.apache.flume.Event;

import backtype.storm.tuple.Tuple;

/**
 * @author Ravikumar Visweswara
 */

public interface AvroFlumeEventProducer extends Serializable {
	public Event toEvent(Tuple input) throws Exception;
}
