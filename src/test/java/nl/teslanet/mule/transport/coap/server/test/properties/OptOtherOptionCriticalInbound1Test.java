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


import org.eclipse.californium.core.coap.Option;
import org.eclipse.californium.core.coap.OptionSet;
import org.junit.Before;


//TODO add observe functionality test
public class OptOtherOptionCriticalInbound1Test extends AbstractInboundPropertyTestcase
{
    private Option option;

    @Before
    public void initializeOption()
    {
        byte[] value= { (byte) 0x12, (byte) 0xFF, (byte) 0x45 };
        option= new Option();
        option.setNumber( 65012 );
        option.setValue( value );
    }

    @Override
    protected void addOption( OptionSet options )
    {
        options.addOption( option );
    }

    @Override
    protected String getPropertyName()
    {
        return "coap.opt.other.65012.critical";
    }

    @Override
    protected Object getExpectedPropertyValue()
    {
        return Boolean.FALSE;

    }

}