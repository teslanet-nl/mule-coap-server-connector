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
package nl.teslanet.mule.transport.coap.server.automation.runner;


import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mule.tools.devkit.ctf.mockup.ConnectorTestContext;

import nl.teslanet.mule.transport.coap.server.CoapServerConnector;


//@RunWith(Suite.class)
//@SuiteClasses
//({ 
//    ResourceExistsTest.class,
//    AddResourceTest.class,
//    RemoveResourceTest.class,
//    ResourceChangedTest.class 
//})
public class FunctionalTestSuite
{

    @BeforeClass
    public static void initialiseSuite()
    {
        ConnectorTestContext.initialize( CoapServerConnector.class );
    }

    @AfterClass
    public static void shutdownSuite()
    {
        ConnectorTestContext.shutDown();
    }

}