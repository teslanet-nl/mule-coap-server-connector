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


import java.util.LinkedList;

import org.eclipse.californium.core.coap.OptionSet;


public class OptLocationQueryOutbound2Test extends AbstractOutboundPropertyTestcase
{
    @Override
    protected String getPropertyName()
    {
        return "coap.opt.location_query";
    }

    @Override
    protected Object fetchOption( OptionSet options )
    {
        return options.getLocationQuery();
    }

    @Override
    protected Object getPropertyValue()
    {
        return new String( "first=1&second=2" );
    }

    @Override
    protected Object getExpectedOptionValue()
    {
        LinkedList< String > list= new LinkedList< String >();
        list.add( "first=1" );
        list.add( "second=2" );

        return list;
    }
}