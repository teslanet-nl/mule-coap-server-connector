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

import nl.teslanet.mule.transport.coap.commons.options.ETag;
import nl.teslanet.mule.transport.coap.commons.options.InvalidETagException;


public class OptIfMatchListInbound1Test extends AbstractInboundPropertyTestcase
{
    @Override
    protected void addOption( OptionSet options ) throws InvalidETagException
    {
        options.addIfMatch( new ETag( "0011FF" ).asBytes() );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.if_match.list";
    }

    @Override
    protected Object getExpectedPropertyValue() throws InvalidETagException
    {
        LinkedList< ETag > list= new LinkedList< ETag >();
        list.add( new ETag( "0011FF" ) );

        return list;
    }
}