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
package nl.teslanet.mule.transport.coap.server.test.properties;


import org.eclipse.californium.core.coap.OptionSet;

//import nl.teslanet.mule.transport.coap.commons.options.InvalidETagException;


public class OptUriPathInbound1Test extends AbstractInboundPropertyTestcase
{

    @Override
    protected void addOption( OptionSet options ) throws Exception
    {
        // nop
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.uri_path";
    }

    @Override
    protected Object getExpectedPropertyValue() throws Exception
    {
        return resourcePath.substring( 1 );
    }
}