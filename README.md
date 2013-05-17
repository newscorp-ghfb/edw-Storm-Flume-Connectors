edw-Storm-Flume-Connectors
==========================

Expedia Storm extension to receive messages from Flume

Flume Storm connector/Flume's Storm Sink/Storm Spout as Flume Source
-------------------------------------------------------------

Class: com.storm.flume.spout.FlumeSourceSpout

following describes various properties supported

property	            default value	           			description
-----------                 ----------------------                      ---------------------------------
flumePropertyPrefix		flume-agent  				Property is used to identify flume related properties for the spout from the Conf object. 
                             		           			use the set method to change the default.
batch-size			100	 
source.type			--					flume source type. Avro is recommended
source.bind			--					hostname or IP where flume source is running. value 0.0.0.0 is recommended
source.port			--					port at which flume source needs to be started
channel.type			--					flume channel type to be used. memory or file is supported
channel.capacity		100 for memory       			The max number of events stored in the channel
                        	1000000 for file	
channel.transactionCapacity	100 for memory
                                1000 for file       			The max number of events stored in the channel per transaction
channel.checkpointDir		~/.flume/file-channel/checkpoint	 only relevant for file channel. The directory where checkpoint file will be stored
channel.dataDirs		~/.flume/file-channel/data		 only relevant for file channel. The directory where log files will be stored


For additional channel properties refer:
http://flume.apache.org/FlumeUserGuide.html#file-channel
http://flume.apache.org/FlumeUserGuide.html#memory-channel


Example configuration:
1) # Example for avro source and memory based channel in storm
flume-agent.channel.type=memory
flume-agent.channel.capacity=2000

flume-agent.source.type=avro
flume-agent.source.bind=0.0.0.0
flume-agent.source.port=10001

2)# Example for avro source and File based channel in storm
flume-agent.channel.type=file
flume-agent.channel.checkpointDir=/var/storm_flume/chk
flume-agent.channel.dataDirs=/var/storm_flume/data

flume-agent.source.type=avro
flume-agent.source.bind=0.0.0.0
flume-agent.source.port=10001


Storm bolt as Flume Avro RPC Sink
---------------------------------------------------

Class:com.storm.flume.bolt.AvroSinkBolt

AvroSinkBolt has been created as a bolt to connect the storm to flume agents or flume sinks.

property		default				description
------------            ------------               ------------------------
flumePropertyPrefix	flume-avro-forward	 
client.type		--			DEFAULT - NettyAvroRpcClient will be used
						DEFAULT_FAILOVER - FailoverRpcClient will be used
						DEFAULT_LOADBALANCE - LoadBalancingRpcClient will be used

Examples:
1) #Bolt to forward messages to flume agents with round_robin load balancing policy
flume-hdfs.client.type=default_loadbalance
flume-hdfs.hosts=h1 h2
flume-hdfs.hosts.h1=myhdfssinkhost1:10002
flume-hdfs.hosts.h2=myhdfssinkhost2:10002
 
2) #load balancing policy. default is round_robin
flume-sql.client.type=default_loadbalance
flume-sql.host-selector=random
flume-sql.hosts=h1 h2
flume-sql.hosts.h1=mysqlsinkhost1:10001
flume-sql.hosts.h2=mysqlsinkhost2:10001






