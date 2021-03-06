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
package nl.teslanet.mule.transport.coap.server.test.secure;


public class SecureClientTest extends AbstractSecureClientTestCase
{
    @Override
    protected String getConfigResources()
    {
        return "mule-config/secure/testserver11.xml";
    };
    
    @Override
    protected int getPort()
    {
        return 5686;
    }
}