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

import nl.teslanet.mule.transport.coap.commons.options.InvalidETagException;


public class OptUriPathListInbound1Test extends AbstractInboundPropertyTestcase
{
    @Override
    protected void addOption( OptionSet options ) throws InvalidETagException
    {
        //nop
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.uri_path.list";
    }

    @Override
    protected Object getExpectedPropertyValue() throws InvalidETagException
    {
        String[] resources= resourcePath.substring( 1 ).split( "/" );

        LinkedList< String > list= new LinkedList< String >();
        for ( String resource : resources )
        {
            list.add( resource );
        }
        return list;
    }
}