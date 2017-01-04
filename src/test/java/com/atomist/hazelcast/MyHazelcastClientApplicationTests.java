package com.atomist.hazelcast;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {MyHazelcastClientApplication.class})
public class MyHazelcastClientApplicationTests {

	private static HazelcastInstance hazelcastServer;

	@Autowired
	private ClientConfig clientConfig;
	@Autowired(required=false)
	private HazelcastInstance hazelcastClient;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Config config = new Config();
		
		config.getNetworkConfig().getJoin().getMulticastConfig().setEnabled(false);
		config.getNetworkConfig().getJoin().getTcpIpConfig().setEnabled(true);
	
		config.getProperties().setProperty("hazelcast.logging.type", "slf4j");
		
		hazelcastServer = Hazelcast.newHazelcastInstance(config);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		HazelcastClient.shutdownAll();
		Hazelcast.shutdownAll();
	}

	@Before
	public void setUp() throws Exception {
		if (this.hazelcastClient==null) {
			// May be auto-started by future releases
			this.hazelcastClient = HazelcastClient.newHazelcastClient(this.clientConfig);
		}
	}

	@Test
	public void count_hazelcasts_in_this_jvm() {
		assertThat("One client",
				HazelcastClient.getAllHazelcastClients().size(), equalTo(1));
		assertThat("One server",
				Hazelcast.getAllHazelcastInstances().size(), equalTo(1));
	}
	
	@Test
	public void count_connected_clients() {
		assertThat(hazelcastServer.getClientService().getConnectedClients().size(), equalTo(1));
	}
		
}
