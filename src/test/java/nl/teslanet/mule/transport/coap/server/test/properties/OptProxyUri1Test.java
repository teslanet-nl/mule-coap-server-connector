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


//TODO configuration option isProxy and automatic  5.05 (Proxying Not Supported (when false)) 
public class OptProxyUri1Test extends AbstractInboundPropertyTestcase
{

    @Override
    protected void addOption( OptionSet options )
    {
        options.setProxyUri( "coap://some.server.org/test" );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.proxy_uri";
    }

    @Override
    protected Object getExpectedPropertyValue()
    {
        return new String( "coap://some.server.org/test" );
    }

}