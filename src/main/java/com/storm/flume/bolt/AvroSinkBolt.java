package com.storm.flume.bolt;

/**
 * @author Ravikumar Visweswara
 */
import java.util.Map;
import java.util.Properties;

import org.apache.flume.Event;
import org.apache.flume.FlumeException;
import org.apache.flume.api.RpcClient;
import org.apache.flume.api.RpcClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.storm.flume.producer.AvroFlumeEventProducer;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

@SuppressWarnings("serial")
public class AvroSinkBolt implements IRichBolt {

    private static final Logger LOG = LoggerFactory.getLogger(AvroSinkBolt.class);
    public static final String DEFAULT_FLUME_PROPERTY_PREFIX = "flume-avro-forward";
    
    private Properties sinkProperties;
    private RpcClient client;
    private AvroFlumeEventProducer producer;
    private OutputCollector collector;
    private String flumePropertyPrefix = DEFAULT_FLUME_PROPERTY_PREFIX;

    public String getFlumePropertyPrefix() {
		return flumePropertyPrefix;
	}

	public void setFlumePropertyPrefix(String flumePropertPrefix) {
		this.flumePropertyPrefix = flumePropertPrefix;
	}

	public AvroFlumeEventProducer getProducer() {
        return producer;
    }

    public void setProducer(AvroFlumeEventProducer producer) {
        this.producer = producer;
    }

    @SuppressWarnings("rawtypes")
    public void prepare(Map config, TopologyContext context, OutputCollector collector) {
    	
        this.collector = collector;
        sinkProperties = new Properties();
        LOG.info("Looking for flume properties");
		for (Object key : config.keySet()) {
			if (key.toString().startsWith(this.getFlumePropertyPrefix())) {
				LOG.info("Found:Key:" + key.toString() + ":" + (String) config.get(key));
				sinkProperties.put(
							key.toString().replace(this.getFlumePropertyPrefix() + ".",
									""), (String) config.get(key));
			}
		}
		
        try {
            createConnection();
            LOG.info("Created connections for flume sources");
        } catch (Exception e) {
            LOG.error("Error creating RpcClient connection.", e);
            destroyConnection();
        }
    }

    public void execute(Tuple input) {

        try {
            verifyConnection();
            Event event = this.producer.toEvent(input);
            LOG.info("Event Created: " + event.toString() + ":MSG:" + new String(event.getBody()));
            if (event != null) {
                this.client.append(event);
                this.collector.ack(input);
            }
        } catch (Exception e) {
            LOG.warn("Failing tuple: " + input);
            LOG.warn("Exception: ", e);
            this.collector.fail(input);
        }
    }

    @Override
    public void cleanup() {
    	LOG.info("Executing cleanup");
    	destroyConnection();
    }

    @Override
    public void declareOutputFields(OutputFieldsDeclarer declarer) {
    }

    @Override
    public Map<String, Object> getComponentConfiguration() {
        return null;
    }

    private void createConnection() throws Exception {
        if (client == null) {
            LOG.info("Avro Bolt: Building RpcClient with properties:" + sinkProperties.toString());
            if(this.sinkProperties == null){
            	throw new FlumeException("Flume Avro Source properties are not set");
            }
            try {
                this.client = RpcClientFactory.getInstance(this.sinkProperties);
            } catch (Exception e) {
                LOG.error("Error creating RpcClient with properties:" + sinkProperties.toString());
                throw e;
            }
            LOG.info("Avro Bolt Created RpcClient with properties:" + sinkProperties.toString());
        }
    }

    private void destroyConnection() {
        if (client != null) {
            LOG.info("Avro Bolt: Closing RpcClient");
            try {
                this.client.close();
            } catch (Exception e) {
                LOG.error("Error closing RpcClient");
            }
            client = null;
            LOG.info("Closed RpcClient with hostname:");
        }
    }

    private void verifyConnection() throws Exception {
        try {
            if (client == null) {
                createConnection();
            } else if (!client.isActive()) {
                destroyConnection();
                createConnection();
            }
        } catch (Exception e) {
            LOG.error("Error verifing RpcClient");
        }
    }
}
