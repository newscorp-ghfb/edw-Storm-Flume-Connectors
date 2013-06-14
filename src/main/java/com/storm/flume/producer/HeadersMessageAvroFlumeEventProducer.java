package com.storm.flume.producer;

import backtype.storm.tuple.Tuple;
import com.storm.flume.common.Constants;
import org.apache.flume.Event;
import org.apache.flume.event.EventBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * @author Ravikumar Visweswara
 */

@SuppressWarnings("serial")
public class HeadersMessageAvroFlumeEventProducer implements AvroFlumeEventProducer {

    private static final Logger LOG = LoggerFactory.getLogger(HeadersMessageAvroFlumeEventProducer.class);

    private static final String CHARSET = "UTF-8";

    public static String getCharset() {
        return CHARSET;
    }

    @SuppressWarnings("unchecked")
    public Event toEvent(Tuple input) throws Exception {

        Map<String, String> headers = null;
        Object headerObj = null;
        String messageStr = null;

		/*If the number of parameters are two, they are assumed as headers and Message
		 *For any other types of input will be thrown an error.
		 */
        if (input.size() == 2) {
            headerObj = input.getValueByField(Constants.HEADERS);
            headers = (Map<String, String>) headerObj;
            messageStr = input.getStringByField(Constants.MESSAGE);
        } else {
            throw new IllegalStateException("Wrong format of touple expected 2. But found " + input.size());
        }

        try {
            LOG.debug("HeadersMessageAvroFlumeEventProducer:MSG:" + messageStr);

            Event event = EventBuilder.withBody(messageStr.getBytes(), headers);
            return event;
        } catch (Exception e) {
            throw e;
        }
    }

}
