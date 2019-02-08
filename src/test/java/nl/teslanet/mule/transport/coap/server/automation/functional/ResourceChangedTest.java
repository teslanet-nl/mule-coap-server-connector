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
package nl.teslanet.mule.transport.coap.server.automation.functional;


import static org.junit.Assert.*;
import nl.teslanet.mule.transport.coap.server.CoapServerConnector;
import nl.teslanet.mule.transport.coap.server.error.ResourceUriException;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;


public class ResourceChangedTest extends AbstractTestCase< CoapServerConnector >
{

    public ResourceChangedTest()
    {
        super( CoapServerConnector.class );
    }

    @Before
    public void setup() throws ResourceUriException
    {
        String uri= "/test";
        boolean get= true;
        boolean put= true;
        boolean post= true;
        boolean delete= true;
        boolean observable= true;
        boolean earlyAck= false;
        java.lang.String title= "test resource";
        java.lang.String ifdesc= null;
        java.lang.String rt= null;
        java.lang.String sz= null;
        java.lang.String ct= null;

        getConnector().addResource( uri, get, put, post, delete, observable, earlyAck, title, ifdesc, rt, sz, ct );
    }

    @After
    public void tearDown()
    {
        // TODO
    }

    @Ignore
    @Test
    public void verify() throws ResourceUriException
    {
        //void expected;
        java.lang.String uri= "/test";
        getConnector().resourceChanged( uri );
        assertEquals( "todo", true, true );
    }

}