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

import org.junit.After;
import org.junit.Before;
import org.mule.tools.devkit.ctf.junit.AbstractTestCase;

import nl.teslanet.mule.transport.coap.server.CoapServerConnector;


/**
 * @author rogier
 *
 */
public class AbstractCoapTestCase extends AbstractTestCase< CoapServerConnector >
{
    public AbstractCoapTestCase()
    {
        super( CoapServerConnector.class );
    }

    @Before
    public void setup()
    {
        // nop
    }

    @After
    public void tearDown()
    {
        // nop
    }

}
