edw-Storm-Flume-Connectors
==========================

Expedia Storm extension to receive messages from Flume

Storm Spout as Flume Source
-------------------------------------------------------------

Class: com.storm.flume.spout.FlumeSourceSpout

Following describes various properties supported in the spout

<table>
  <tbody>
    <tr>
      <th>property</th>
      <th>default value</th>
      <th>description</th>
    </tr>
    <tr>
      <td>flumePropertyPrefix</td>
      <td>flume-agent</td>
      <td>
        <p>this property is used to find the flume related properties from the config file or Conf object.</p>
        <p>use the set method to change the default.</p>
      </td>
    </tr>
    <tr>
      <td>batch-size</td>
      <td>100</td>
      <td> </td>
    </tr>
    <tr>
      <td colspan="1">source.type</td>
      <td colspan="1">--</td>
      <td colspan="1">flume source type. Avro is recommended</td>
    </tr>
    <tr>
      <td colspan="1">source.bind</td>
      <td colspan="1">--</td>
      <td colspan="1">hostname or IP where flume source is running. e.g 0.0.0.0</td>
    </tr>
    <tr>
      <td colspan="1">source.port</td>
      <td colspan="1">--</td>
      <td colspan="1">port at which flume source needs to be started</td>
    </tr>
    <tr>
      <td colspan="1">channel.type</td>
      <td colspan="1">--</td>
      <td colspan="1">flume channel type to be used. memory or file is supported</td>
    </tr>
    <tr>
      <td colspan="1">channel.capacity</td>
      <td colspan="1">
        <p>100 for memory</p>
        <p>1000000 for file</p>
      </td>
      <td colspan="1">The max number of events stored in the channel</td>
    </tr>
    <tr>
      <td colspan="1">channel.transactionCapacity</td>
      <td colspan="1">
        <p>100 for memory</p>
        <p>1000 for file</p>
      </td>
      <td colspan="1">The max number of events stored in the channel per transaction</td>
    </tr>
    <tr>
      <td colspan="1">channel.checkpointDir</td>
      <td colspan="1">~/.flume/file-channel/checkpoint</td>
      <td colspan="1"> <strong>only relevant for file channel</strong>. The directory where checkpoint file will be stored</td>
    </tr>
    <tr>
      <td colspan="1">channel.dataDirs</td>
      <td colspan="1">~/.flume/file-channel/data</td>
      <td colspan="1">
        <strong>only relevant for file channel</strong>. The directory where log files will be stored</td>
    </tr>
  </tbody>
</table>

<p>For additional channel properties refer:</p>
http://flume.apache.org/FlumeUserGuide.html#file-channel
http://flume.apache.org/FlumeUserGuide.html#memory-channel


<p>Example configuration:</p>
1) # Example for avro source and memory based channel in storm
flume-agent.channel.type=memory <br/>
flume-agent.channel.capacity=2000 <br/>

flume-agent.source.type=avro <br/>
flume-agent.source.bind=0.0.0.0 <br/>
flume-agent.source.port=10001 <br/>

2)# Example for avro source and File based channel in storm <br/>
flume-agent.channel.type=file <br/>
flume-agent.channel.checkpointDir=/var/storm_flume/chk <br/>
flume-agent.channel.dataDirs=/var/storm_flume/data <br/>

flume-agent.source.type=avro <br/>
flume-agent.source.bind=0.0.0.0 <br/>
flume-agent.source.port=10001 <br/>


Storm bolt as Flume Avro RPC Sink
---------------------------------------------------

Class:com.storm.flume.bolt.AvroSinkBolt

AvroSinkBolt has been created as a bolt to connect the storm to flume agents or flume sinks.

<table>
  <tbody>
    <tr>
      <th>property</th>
      <th>default</th>
      <th>description</th>
    </tr>
    <tr>
      <td>flumePropertyPrefix</td>
      <td>flume-avro-forward</td>
      <td> </td>
    </tr>
    <tr>
      <td>client.type</td>
      <td>
        --
      </td>
      <td>
        <p>DEFAULT - NettyAvroRpcClient will be used</p>
        <p>DEFAULT_FAILOVER - FailoverRpcClient will be used</p>
        <p>DEFAULT_LOADBALANCE - LoadBalancingRpcClient will be used</p>
      </td>
    </tr>
  </tbody>
</table>

Examples:
1) #Bolt to forward messages to flume agents with round_robin load balancing policy <br/>
flume-hdfs.client.type=default_loadbalance <br/>
flume-hdfs.hosts=h1 h2 <br/>
flume-hdfs.hosts.h1=myhdfssinkhost1:10002 <br/>
flume-hdfs.hosts.h2=myhdfssinkhost2:10002 <br/>
 
2) #load balancing policy. default is round_robin <br/>
flume-sql.client.type=default_loadbalance <br/>
flume-sql.host-selector=random <br/>
flume-sql.hosts=h1 h2 <br/>
flume-sql.hosts.h1=mysqlsinkhost1:10001 <br/>
flume-sql.hosts.h2=mysqlsinkhost2:10001 <br/>






