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

//import nl.teslanet.mule.transport.coap.commons.options.InvalidETagException;


public class OptLocationPathListOutbound1Test extends AbstractOutboundPropertyTestcase
{
    @Override
    protected String getPropertyName()
    {
        return "coap.opt.location_path.list";
    }

    @Override
    protected Object fetchOption( OptionSet options )
    {
        return options.getLocationPath();
    }

    @Override
    protected Object getPropertyValue() throws Exception
    {
        LinkedList< String > list= new LinkedList< String >();
        list.add( "test" );
        list.add( "this" );
        list.add( "path" );

        return list;
    }

    @Override
    protected Object getExpectedOptionValue() throws Exception
    {
        LinkedList< String > list= new LinkedList< String >();
        list.add( "test" );
        list.add( "this" );
        list.add( "path" );

        return list;
    }

}