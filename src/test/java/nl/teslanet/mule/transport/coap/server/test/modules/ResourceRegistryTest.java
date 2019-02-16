/*******************************************************************************
 * Copyright (c) 2017, 2018, 2019 (teslanet.nl) Rogier Cobben.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License - v 2.0 
 * which accompanies this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *    (teslanet.nl) Rogier Cobben - initial creation
 ******************************************************************************/
package nl.teslanet.mule.transport.coap.server.test.modules;


import static org.junit.Assert.*;

import java.util.List;

import org.eclipse.californium.core.CoapResource;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import nl.teslanet.mule.transport.coap.server.CoapServerConnector;
import nl.teslanet.mule.transport.coap.server.Listener;
import nl.teslanet.mule.transport.coap.server.ResourceRegistry;
import nl.teslanet.mule.transport.coap.server.ServedResource;
import nl.teslanet.mule.transport.coap.server.config.ResourceConfig;
import nl.teslanet.mule.transport.coap.server.error.ResourceUriException;
import nl.teslanet.mule.transport.coap.server.generated.sources.ListenMessageSource;


public class ResourceRegistryTest
{
    @Rule
    public ExpectedException exception= ExpectedException.none();
	private CoapServerConnector connector;

    @Before
    public void setUp() throws Exception
    {
    }

    @After
    public void tearDown() throws Exception
    {
    	connector= new CoapServerConnector();
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
    public void testAddResourceWithoutName() throws ResourceUriException
    {
        CoapResource root= new CoapResource( "" );
        ResourceRegistry registry= new ResourceRegistry( root );
        ResourceConfig resourceConfig;

        resourceConfig= new ResourceConfig();
        //resourceConfig.setName( "resource" );

        exception.expect( NullPointerException.class );
        exception.expectMessage( "Child must have a name" );
        
        ServedResource resource= new ServedResource(connector, resourceConfig);
        registry.add( null, resource );
    }

    @Test
    public void testAddResource() throws ResourceUriException
    {
        CoapResource root= new CoapResource( "" );
        ResourceRegistry registry= new ResourceRegistry( root );
        ResourceConfig resourceConfig;
        String name1= "resource1";
        String uri1= "/resource1";
        String name2= "resource2";
        String uri2= "/resource1/resource2";
        String name3= "resource3";
        String uri3= "/resource1/resource2/resource3";
        String name4= "resource4";
        String uri4= "/resource1/resource4";

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name1 );
        ServedResource resource= new ServedResource(connector, resourceConfig);
        registry.add( null, resource );
        assertNotNull( registry );
        assertEquals( "registry does not contain resource1", uri1, registry.getResource( uri1 ).getURI() );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name2 );
        ServedResource parent1;
        parent1= resource;
        resource= new ServedResource(connector, resourceConfig);
        registry.add( parent1, resource );
        assertNotNull( registry );
        assertEquals( "registry does not contain resource2", uri2, registry.getResource( uri2 ).getURI() );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name3 );
        ServedResource parent2;
        parent2= resource;
        resource= new ServedResource(connector, resourceConfig);
        registry.add( parent2, resource );
        assertNotNull( registry );
        assertEquals( "registry does not contain resource3", uri3, registry.getResource( uri3 ).getURI() );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name4 );
        registry.add( parent1, resource );
        assertNotNull( registry );
        assertEquals( "registry does not contain resource4", uri4, registry.getResource( uri4 ).getURI() );
    }

    @Test
    public void testRemoveResource1() throws ResourceUriException
    {
        CoapResource root= new CoapResource( "" );
        ResourceRegistry registry= new ResourceRegistry( root );
        ResourceConfig resourceConfig;
        String name1= "resource1";
        String uri1= "/resource1";
        String name2= "resource2";
        String uri2= "/resource1/resource2";
        String name3= "resource3";
        String uri3= "/resource1/resource2/resource3";
        String name4= "resource4";
        String uri4= "/resource1/resource4";

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name1 );
        ServedResource resource= new ServedResource(connector, resourceConfig);
        registry.add( null, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name2 );
        ServedResource parent1;
        parent1= resource;
        resource= new ServedResource(connector, resourceConfig);
        registry.add( parent1, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name3 );
        ServedResource parent2;
        parent2= resource;
        resource= new ServedResource(connector, resourceConfig);
        registry.add( parent2, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name4 );
        resource= new ServedResource(connector, resourceConfig);
        registry.add( parent1, resource );

        assertEquals( "registry does not contain resource1", uri1, registry.getResource( uri1 ).getURI() );
        assertEquals( "registry does not contain resource2", uri2, registry.getResource( uri2 ).getURI() );
        assertEquals( "registry does not contain resource3", uri3, registry.getResource( uri3 ).getURI() );
        assertEquals( "registry does not contain resource4", uri4, registry.getResource( uri4 ).getURI() );

        registry.remove( resource);

        assertEquals( "registry does not contain resource1", uri1, registry.getResource( uri1 ).getURI() );
        assertEquals( "registry does not contain resource2", uri2, registry.getResource( uri2 ).getURI() );
        assertEquals( "registry does not contain resource3", uri3, registry.getResource( uri3 ).getURI() );
        assertTrue( "registry must not contain resource3", registry.findResources( uri4 ).isEmpty() );
    }

    @Test
    public void testRemoveResource2() throws ResourceUriException
    {
        CoapResource root= new CoapResource( "" );
        ResourceRegistry registry= new ResourceRegistry( root );
        ResourceConfig resourceConfig;
        String name1= "resource1";
        String uri1= "/resource1";
        String name2= "resource2";
        String uri2= "/resource1/resource2";
        String name3= "resource3";
        String uri3= "/resource1/resource2/resource3";
        String name4= "resource4";
        String uri4= "/resource1/resource4";

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name1 );
        ServedResource resource= new ServedResource(connector, resourceConfig);
        registry.add( null, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name2 );
        ServedResource parent1;
        parent1= resource;
        resource= new ServedResource(connector, resourceConfig);
        registry.add( parent1, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name3 );
        ServedResource parent2;
        parent2= resource;
        ServedResource resource2= new ServedResource(connector, resourceConfig);
        registry.add( parent2, resource2 );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name4 );
        registry.add( parent1, resource );

        assertEquals( "registry does not contain resource1", uri1, registry.getResource( uri1 ).getURI() );
        assertEquals( "registry does not contain resource2", uri2, registry.getResource( uri2 ).getURI() );
        assertEquals( "registry does not contain resource3", uri3, registry.getResource( uri3 ).getURI() );
        assertEquals( "registry does not contain resource4", uri4, registry.getResource( uri4 ).getURI() );

        registry.remove( resource );

        assertEquals( "registry does not contain resource1", uri1, registry.getResource( uri1 ).getURI() );
        assertTrue( "registry must not contain resource2", registry.findResources( uri2 ).isEmpty() );
        assertTrue( "registry must not contain resource3", registry.findResources( uri3 ).isEmpty() );
        assertEquals( "registry does not contain resource4", uri4, registry.getResource( uri4 ).getURI() );
    }

    @Test
    public void testAddListener() throws ResourceUriException
    {
        ListenMessageSource callback= new ListenMessageSource( "operation1" );
        CoapResource root= new CoapResource( "" );
        ResourceRegistry registry= new ResourceRegistry( root );
        Listener listener;
        String uri1= "/resource1";
        String uri2= "/resource1/resource2";
        String uri3= "/resource1/resource2/resource3";
        String uri4= "/resource1/resource4";

        //see that no exceptions occur
        listener= new Listener( uri1, callback );
        registry.add( listener );

        listener= new Listener( uri2, callback );
        registry.add( listener );

        listener= new Listener( uri3, callback );
        registry.add( listener );

        listener= new Listener( uri4, callback );
        registry.add( listener );
    }

    @Test
    public void testCallBack() throws ResourceUriException
    {
        CoapResource root= new CoapResource( "" );
        ResourceRegistry registry= new ResourceRegistry( root );
        ListenMessageSource callback1= new ListenMessageSource( "operation1" );
        ListenMessageSource callback2= new ListenMessageSource( "operation2" );
        ListenMessageSource callback3= new ListenMessageSource( "operation3" );
        ListenMessageSource callback4= new ListenMessageSource( "operation4" );
        ResourceConfig resourceConfig;
        String name1= "resource1";
        String uri1= "/resource1";
        String name2= "resource2";
        String uri2= "/resource1/resource2";
        String name3= "resource3";
        String uri3= "/resource1/resource2/resource3";
        String name4= "resource4";
        String uri4= "/resource1/resource4";

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name1 );
        ServedResource resource= new ServedResource(connector, resourceConfig);
        registry.add( null, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name2 );
        ServedResource parent1;
        parent1= resource;
        resource= new ServedResource(connector, resourceConfig);
        registry.add( parent1, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name3 );
        ServedResource parent2;
        parent2= resource;
        resource= new ServedResource(connector, resourceConfig);
        registry.add( parent2, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name4 );
        registry.add( parent1, resource );

        Listener listener;
        listener= new Listener( uri1, callback1 );
        registry.add( listener );

        listener= new Listener( uri2, callback2 );
        registry.add( listener );

        listener= new Listener( uri3, callback3 );
        registry.add( listener );

        listener= new Listener( uri4, callback4 );
        registry.add( listener );

        assertEquals( "resource1 has wrong callback", callback1, registry.getResource( uri1 ).getCallback() );
        assertEquals( "resource2 has wrong callback", callback2, registry.getResource( uri2 ).getCallback() );
        assertEquals( "resource3 has wrong callback", callback3, registry.getResource( uri3 ).getCallback() );
        assertEquals( "resource4 has wrong callback", callback4, registry.getResource( uri4 ).getCallback() );
    }

    @Test
    public void testCallBackWithWildcard1() throws ResourceUriException
    {
        CoapResource root= new CoapResource( "" );
        ResourceRegistry registry= new ResourceRegistry( root );
        ListenMessageSource callback1= new ListenMessageSource( "operation1" );
        ListenMessageSource callback2= new ListenMessageSource( "operation2" );
        ResourceConfig resourceConfig;
        String name1= "resource1";
        String uri1= "/resource1";
        String name2= "resource2";
        String uri2= "/resource1/resource2";
        String name3= "resource3";
        String uri3= "/resource1/resource2/resource3";
        String name4= "resource4";
        String uri4= "/resource1/resource4";

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name1 );
        ServedResource resource= new ServedResource(connector, resourceConfig);
        registry.add( null, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name2 );
        ServedResource parent1;
        parent1= resource;
        resource= new ServedResource(connector, resourceConfig);
        registry.add( parent1, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name3 );
        ServedResource parent2;
        parent2= resource;
        resource= new ServedResource(connector, resourceConfig);
        registry.add( parent2, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name4 );
        registry.add( parent1, resource );

        Listener listener;
        listener= new Listener( "/*", callback1 );
        registry.add( listener );

        listener= new Listener( uri2, callback2 );
        registry.add( listener );

        assertEquals( "resource1 has wrong callback", callback1, registry.getResource( uri1 ).getCallback() );
        assertEquals( "resource2 has wrong callback", callback2, registry.getResource( uri2 ).getCallback() );
        assertEquals( "resource3 has wrong callback", callback1, registry.getResource( uri3 ).getCallback() );
        assertEquals( "resource4 has wrong callback", callback1, registry.getResource( uri4 ).getCallback() );
    }

    @Test
    public void testCallBackWithWildcard2() throws ResourceUriException
    {
        CoapResource root= new CoapResource( "" );
        ResourceRegistry registry= new ResourceRegistry( root );
        ListenMessageSource callback1= new ListenMessageSource( "operation1" );
        ListenMessageSource callback3= new ListenMessageSource( "operation3" );
        ListenMessageSource callback4= new ListenMessageSource( "operation4" );
        ResourceConfig resourceConfig;
        String name1= "resource1";
        String uri1= "/resource1";
        String name2= "resource2";
        String uri2= "/resource1/resource2";
        String name3= "resource3";
        String uri3= "/resource1/resource2/resource3";
        String name4= "resource4";
        String uri4= "/resource1/resource4";

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name1 );
        ServedResource resource= new ServedResource(connector, resourceConfig);
        registry.add( null, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name2 );
        ServedResource parent1;
        parent1= resource;
        resource= new ServedResource(connector, resourceConfig);
        registry.add( parent1, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name3 );
        ServedResource parent2;
        parent2= resource;
        resource= new ServedResource(connector, resourceConfig);
        registry.add( parent2, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name4 );
        registry.add( parent1, resource );

        Listener listener;
        listener= new Listener( "/*", callback1 );
        registry.add( listener );

        listener= new Listener( "/resource1/resource2/*", callback3 );
        registry.add( listener );

        listener= new Listener( uri4, callback4 );
        registry.add( listener );

        assertEquals( "resource1 has wrong callback", callback1, registry.getResource( uri1 ).getCallback() );
        assertEquals( "resource2 has wrong callback", callback1, registry.getResource( uri2 ).getCallback() );
        assertEquals( "resource3 has wrong callback", callback3, registry.getResource( uri3 ).getCallback() );
        assertEquals( "resource4 has wrong callback", callback4, registry.getResource( uri4 ).getCallback() );
    }

    @Test
    public void testCallBackWithWildcard3() throws ResourceUriException
    {
        CoapResource root= new CoapResource( "" );
        ResourceRegistry registry= new ResourceRegistry( root );
        ListenMessageSource callback1= new ListenMessageSource( "operation1" );
        ListenMessageSource callback3= new ListenMessageSource( "operation3" );
        ListenMessageSource callback4= new ListenMessageSource( "operation4" );
        ResourceConfig resourceConfig;
        String name1= "resource1";
        String uri1= "/resource1";
        String name2= "resource2";
        String uri2= "/resource1/resource2";
        String name3= "resource3";
        String uri3= "/resource1/resource2/resource3";
        String name4= "resource4";
        String uri4= "/resource1/resource4";

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name1 );
        ServedResource resource= new ServedResource(connector, resourceConfig);
        registry.add( null, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name2 );
        ServedResource parent1;
        parent1= resource;
        resource= new ServedResource(connector, resourceConfig);
        registry.add( parent1, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name3 );
        ServedResource parent2;
        parent2= resource;
        resource= new ServedResource(connector, resourceConfig);
        registry.add( parent2, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name4 );
        registry.add( parent1, resource );

        Listener listener;
        listener= new Listener( uri1, callback1 );
        registry.add( listener );

        listener= new Listener( "/resource1/resource2/*", callback3 );
        registry.add( listener );

        listener= new Listener( uri4, callback4 );
        registry.add( listener );

        assertEquals( "resource1 has wrong callback", callback1, registry.getResource( uri1 ).getCallback() );
        assertEquals( "resource2 must not have callback", null, registry.getResource( uri2 ).getCallback() );
        assertEquals( "resource3 has wrong callback", callback3, registry.getResource( uri3 ).getCallback() );
        assertEquals( "resource4 has wrong callback", callback4, registry.getResource( uri4 ).getCallback() );
    }

    @Test
    public void testGetResourceNonexistent() throws ResourceUriException
    {
        CoapResource root= new CoapResource( "" );
        ResourceRegistry registry= new ResourceRegistry( root );
        ResourceConfig resourceConfig;
        String name1= "resource1";
//        String uri1= "/resource1";
        String name2= "resource2";
//        String uri2= "/resource1/resource2";
        String name3= "resource3";
        String uri4= "/resource1/resource4";

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name1 );
        ServedResource resource= new ServedResource(connector, resourceConfig);
        registry.add( null, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name2 );
        ServedResource parent1;
        parent1= resource;
        resource= new ServedResource(connector, resourceConfig);
        registry.add( parent1, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name3 );
        ServedResource parent2;
        parent2= resource;
        resource= new ServedResource(connector, resourceConfig);
        registry.add( parent2, resource );

        exception.expect( ResourceUriException.class );
        exception.expectMessage( uri4 );
        exception.expectMessage( "resource does not exist" );

        assertFalse( "registry must not contain resource4", uri4.equals( registry.getResource( uri4 ).getURI() ) );

    }

    @Test
    public void testGetResource() throws ResourceUriException
    {
        CoapResource root= new CoapResource( "" );
        ResourceRegistry registry= new ResourceRegistry( root );
        ResourceConfig resourceConfig;
        String name1= "resource1";
        String uri1= "/resource1";
        String name2= "resource2";
        String uri2= "/resource1/resource2";
        String name3= "resource3";
        String uri3= "/resource1/resource2/resource3";
        String name4= "resource4";
        String uri4= "/resource1/resource4";

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name1 );
        ServedResource resource= new ServedResource(connector, resourceConfig);
        registry.add( null, resource );
        assertNotNull( registry );
        assertEquals( "registry does not contain resource1", uri1, registry.getResource( uri1 ).getURI() );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name2 );
        ServedResource parent1;
        parent1= resource;
        resource= new ServedResource(connector, resourceConfig);
        registry.add( parent1, resource );
        assertNotNull( registry );
        assertEquals( "registry does not contain resource2", uri2, registry.getResource( uri2 ).getURI() );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name3 );
        ServedResource parent2;
        parent2= resource;
        resource= new ServedResource(connector, resourceConfig);
        registry.add( parent2, resource );
        assertNotNull( registry );
        assertEquals( "registry does not contain resource3", uri3, registry.getResource( uri3 ).getURI() );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name4 );
        registry.add( parent1, resource );
        assertNotNull( registry );
        assertEquals( "registry does not contain resource4", uri4, registry.getResource( uri4 ).getURI() );
    }

    @Test
    public void testFindResources() throws ResourceUriException
    {
        CoapResource root= new CoapResource( "" );
        ResourceRegistry registry= new ResourceRegistry( root );
        ResourceConfig resourceConfig;
        String name1= "resource1";
        String uri1= "/resource1";
        String name2= "resource2";
        String uri2= "/resource1/resource2";
        String name3= "resource3";
        String uri3= "/resource1/resource2/resource3";
        String name4= "resource4";
        String uri4= "/resource1/resource4";

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name1 );
        ServedResource resource= new ServedResource(connector, resourceConfig);
        registry.add( null, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name2 );
        ServedResource parent1;
        parent1= resource;
        resource= new ServedResource(connector, resourceConfig);
        registry.add( parent1, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name3 );
        ServedResource parent2;
        parent2= resource;
        resource= new ServedResource(connector, resourceConfig);
        registry.add( parent2, resource );

        resourceConfig= new ResourceConfig();
        resourceConfig.setName( name4 );
        registry.add( parent1, resource );

        List< ServedResource > resources;

        resources= registry.findResources( uri1 );
        assertNotNull( resources );
        assertEquals( "wrong resource count", 1, resources.size() );
        assertEquals( "wrong resources found", 0x1, resourcesPresent( resources ) );

        resources= registry.findResources( uri2 );
        assertNotNull( resources );
        assertEquals( "wrong resource count", 1, resources.size() );
        assertEquals( "wrong resources found", 0x2, resourcesPresent( resources ) );

        resources= registry.findResources( uri3 );
        assertNotNull( resources );
        assertEquals( "wrong resource count", 1, resources.size() );
        assertEquals( "wrong resources found", 0x4, resourcesPresent( resources ) );

        resources= registry.findResources( uri4 );
        assertNotNull( resources );
        assertEquals( "wrong resource count", 1, resources.size() );
        assertEquals( "wrong resources found", 0x8, resourcesPresent( resources ) );

        resources= registry.findResources( "/*" );
        assertNotNull( resources );
        assertEquals( "wrong resource count", 4, resources.size() );
        assertEquals( "wrong resources found", 0x1 | 0x2 | 0x4 | 0x8, resourcesPresent( resources ) );

        resources= registry.findResources( "/resource1/*" );
        assertNotNull( resources );
        assertEquals( "wrong resource count", 3, resources.size() );
        assertEquals( "wrong resources found", 0x2 | 0x4 | 0x8, resourcesPresent( resources ) );

        resources= registry.findResources( "/resource1/resource2/*" );
        assertNotNull( resources );
        assertEquals( "wrong resource count", 1, resources.size() );
        assertEquals( "wrong resources found", 0x4, resourcesPresent( resources ) );

        resources= registry.findResources( "/resource1/resource2/resource3/*" );
        assertNotNull( resources );
        assertEquals( "wrong resource count", 0, resources.size() );
        assertEquals( "wrong resources found", 0x0, resourcesPresent( resources ) );

        resources= registry.findResources( "/resource1/resource4/*" );
        assertNotNull( resources );
        assertEquals( "wrong resource count", 0, resources.size() );
        assertEquals( "wrong resources found", 0x0, resourcesPresent( resources ) );

    }

    int resourcesPresent( List< ServedResource > resources )
    {
        final String name1= "resource1";
        final String name2= "resource2";
        final String name3= "resource3";
        final String name4= "resource4";
        int flags= 0;

        for ( ServedResource resource : resources )
        {
            switch ( resource.getName() )
            {
                case name1:
                    flags|= 0x1;
                    break;
                case name2:
                    flags|= 0x2;
                    break;
                case name3:
                    flags|= 0x4;
                    break;
                case name4:
                    flags|= 0x8;
                    break;
            }
        }
        return flags;
    }
}
