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


public class OptSize1Outbound1Test extends AbstractOutboundPropertyTestcase
{
    @Override
    protected String getPropertyName()
    {
        return "coap.opt.size1";
    }

    @Override
    protected Object fetchOption( OptionSet options )
    {
        return options.getSize1();
    }

    @Override
    protected Object getPropertyValue()
    {
        return new Integer( 231 );
    }

    @Override
    protected Object getExpectedOptionValue()
    {
        return new Integer( 231 );
    }
}