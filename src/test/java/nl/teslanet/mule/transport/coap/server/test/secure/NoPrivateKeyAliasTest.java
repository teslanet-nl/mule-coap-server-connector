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
package nl.teslanet.mule.transport.coap.server.test.secure;


import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.isA;
import static org.junit.internal.matchers.ThrowableCauseMatcher.hasCause;
import static org.junit.internal.matchers.ThrowableMessageMatcher.hasMessage;

import org.mule.api.ConnectionException;
import org.mule.api.lifecycle.LifecycleException;

import nl.teslanet.mule.transport.coap.server.error.EndpointConstructionException;
import nl.teslanet.mule.transport.coap.server.test.utils.AbstractMuleStartTestCase;


public class NoPrivateKeyAliasTest extends AbstractMuleStartTestCase
{
    @Override
    protected void expectException()
    {
        exception.expect( isA( LifecycleException.class ) );
        exception.expect( hasMessage( containsString( "nl.teslanet.mule.transport.coap.server" ) ) );
        exception.expect( hasCause( isA( ConnectionException.class ) ) );
        exception.expect( hasCause( hasMessage( containsString( "CoAP configuration error" ) ) ) );
        exception.expect( hasCause( hasCause( isA( EndpointConstructionException.class ) ) ) );
        exception.expect( hasCause( hasCause( hasMessage( containsString( "cannot construct secure endpoint" ) ) ) ) );
        exception.expect( hasCause( hasCause( hasCause( isA( IllegalArgumentException.class ) ) ) ) );
        exception.expect( hasCause( hasCause( hasCause( hasMessage( containsString( "no credentials found for" ) ) ) ) ) );
    }

    @Override
    protected String getConfigResources()
    {
        return "mule-config/secure/testserver4.xml";
    };
}