package nl.teslanet.mule.transport.coap.server.modules.test;


import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

import org.eclipse.californium.core.CoapResource;
import nl.teslanet.mule.transport.coap.server.CoapServerConnector;
import nl.teslanet.mule.transport.coap.server.Listener;
import nl.teslanet.mule.transport.coap.server.ResourceRegistry;
import nl.teslanet.mule.transport.coap.server.ServedResource;
import nl.teslanet.mule.transport.coap.server.config.ResourceConfig;
import nl.teslanet.mule.transport.coap.server.error.ResourceUriException;
import nl.teslanet.mule.transport.coap.server.generated.sources.ListenMessageSource;


public class ResourceRegistryTest
{
    CoapServerConnector coapServerConnector= null;

    @Rule
    public ExpectedException exception= ExpectedException.none();

    @Before
    public void setUp() throws Exception
    {
        coapServerConnector= new CoapServerConnector();
    }

    @After
    public void tearDown() throws Exception
    {

    }

    @Test
    public void testConstructor() throws ResourceUriException
    {
        CoapResource root= new CoapResource( "" );
        ResourceRegistry registry;

        registry= new ResourceRegistry( root );
        assertNotNull( registry );
        assertEquals( "register should not expose root resource", null, registry.getResource( "" ) );
    }

    @Test
    public void testConstructorWithoutRootResource() throws ResourceUriException
    {
        ResourceRegistry registry;

        exception.expect( NullPointerException.class );
        exception.expectMessage( "Cannot construct a ResourceRegistry without root resource" );

        registry= new ResourceRegistry( null );
        assertNotNull( registry );
    }

    @Test
    public void testAddResource() throws ResourceUriException
    {
        CoapResource root= new CoapResource( "" );
        ResourceRegistry registry= new ResourceRegistry( root );
        ResourceConfig resourceConfig1;
        ServedResource resource1;
        ResourceConfig resourceConfig2;
        ServedResource resource2;
        ResourceConfig resourceConfig3;
        ServedResource resource3;
        ResourceConfig resourceConfig4;
        ServedResource resource4;

        resourceConfig1= new ResourceConfig();
        resourceConfig1.setName( "resource1" );
        resource1= new ServedResource( coapServerConnector, resourceConfig1 );
        registry.add( null, resource1 );
        assertNotNull( registry );
        assertEquals( "registry does not contain resource", resource1, registry.getResource( "/resource1" ) );

        resourceConfig2= new ResourceConfig();
        resourceConfig2.setName( "resource2" );
        resource2= new ServedResource( coapServerConnector, resourceConfig2 );
        registry.add( resource1, resource2 );
        assertNotNull( registry );
        assertEquals( "registry does not contain resource2", resource2, registry.getResource( "/resource1/resource2" ) );

        resourceConfig3= new ResourceConfig();
        resourceConfig3.setName( "resource3" );
        resource3= new ServedResource( coapServerConnector, resourceConfig3 );
        registry.add( resource2, resource3 );
        assertNotNull( registry );
        assertEquals( "registry does not contain resource3", resource3, registry.getResource( "/resource1/resource2/resource3" ) );

        resourceConfig4= new ResourceConfig();
        resourceConfig4.setName( "resource4" );
        resource4= new ServedResource( coapServerConnector, resourceConfig4 );
        registry.add( resource1, resource4 );
        assertNotNull( registry );
        assertEquals( "registry does not contain resource3", resource4, registry.getResource( "/resource1/resource4" ) );
    }

    @Test
    public void testAddResourceWithoutName() throws ResourceUriException
    {
        CoapResource root= new CoapResource( "" );
        ResourceRegistry registry= new ResourceRegistry( root );
        ResourceConfig resourceConfig;
        ServedResource resource;

        resourceConfig= new ResourceConfig();
        //resourceConfig.setName( "resource" );
        resource= new ServedResource( coapServerConnector, resourceConfig );

        exception.expect( NullPointerException.class );
        exception.expectMessage( "Child must have a name" );

        registry.add( null, resource );
    }

}
