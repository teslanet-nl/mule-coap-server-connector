package nl.teslanet.mule.transport.coap.server.modules.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.californium.core.CoapResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import nl.teslanet.mule.transport.coap.server.ResourceRegistry;
import nl.teslanet.mule.transport.coap.server.config.ResourceConfig;
import nl.teslanet.mule.transport.coap.server.error.ResourceUriException;


public class ResourceRegistryTest
{
    @Rule
    public ExpectedException exception= ExpectedException.none();

    @Before
    public void setUp() throws Exception
    {
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
        ResourceConfig resourceConfig;
        String name1= "resource1";
        String uri1= "/" + name1;
        String name2= "resource2";
        String uri2= uri1 + "/" + name2;
        String name3= "resource3";
        String uri3= uri2 + "/" + name3;
        String name4= "resource4";
        String uri4= uri1 + "/" + name2;

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name1 );
        registry.add( null, resourceConfig );
        assertNotNull( registry );
        assertEquals( "registry does not contain resource", uri1, registry.getResource( uri1 ).getURI() );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name2 );
        registry.add( uri1, resourceConfig );
        assertNotNull( registry );
        assertEquals( "registry does not contain resource2", uri2, registry.getResource( uri2 ).getURI() );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name3 );
        registry.add( uri2, resourceConfig );
        assertNotNull( registry );
        assertEquals( "registry does not contain resource3", uri3, registry.getResource( uri3 ).getURI() );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name4 );
        registry.add( uri2, resourceConfig );
        assertNotNull( registry );
        assertEquals( "registry does not contain resource3", uri4, registry.getResource( uri4 ).getURI() );
    }

    @Test
    public void testAddResourceWithoutName() throws ResourceUriException
    {
        CoapResource root= new CoapResource( "" );
        ResourceRegistry registry= new ResourceRegistry( root );
        ResourceConfig resourceConfig;

        resourceConfig= new ResourceConfig();
        //resourceConfig.setName( "resource" );

        exception.expect( NullPointerException.class );
        exception.expectMessage( "Child must have a name" );

        registry.add( null, resourceConfig );
    }

}
