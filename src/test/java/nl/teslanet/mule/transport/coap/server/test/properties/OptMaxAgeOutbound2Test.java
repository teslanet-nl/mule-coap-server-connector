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


public class OptMaxAgeOutbound2Test extends AbstractOutboundPropertyTestcase
{
    @Override
    protected String getPropertyName()
    {
        return "coap.opt.max_age";
    }

    @Override
    protected Object fetchOption( OptionSet options )
    {
        return options.getMaxAge();
    }

    @Override
    protected Object getPropertyValue()
    {
        return new Stringable( new Long( 120 ) );
    }

    @Override
    protected Object getExpectedOptionValue()
    {
        return new Long( 120 );
    }
}