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
public class OptUriPort1Test extends AbstractInboundPropertyTestcase
{

    @Override
    protected void addOption( OptionSet options )
    {
        options.setUriPort( 4556 );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.uri_port";
    }

    @Override
    protected Object getExpectedPropertyValue()
    {
        return new Integer( 4556 );
    }

}