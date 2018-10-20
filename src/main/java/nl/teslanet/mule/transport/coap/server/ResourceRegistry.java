/*******************************************************************************
 * Copyright (c) 2017, 2018 (teslanet.nl) Rogier Cobben.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * and Eclipse Public License - v 2.0 which accompany this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *    (teslanet.nl) Rogier Cobben - initial creation
 ******************************************************************************/

package nl.teslanet.mule.transport.coap.server;


import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.californium.core.server.resources.Resource;

import nl.teslanet.mule.transport.coap.commons.Defs;
import nl.teslanet.mule.transport.coap.server.error.ResourceUriException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Registry of served resources and listeners on the resources.
 * It maintains consistency of the callback relations they have.
 */
public class ResourceRegistry
{
    //TODO maybe list, depending on validation of duplication of resourcenames
    private ConcurrentHashMap< String, ServedResource > servedResources;

    private CopyOnWriteArrayList< Listener > listeners;

    Resource root= null;

    /**
     * Construct a registry. The constructor initializes served resources 
     * and listener repositories. 
     * @param root mandatory root resource
     */
    public ResourceRegistry( Resource root )
    {
        if ( root == null ) throw new NullPointerException( "Cannot construct a ResourceRegistry without root resource." );
        this.root= root;

        servedResources= new ConcurrentHashMap< String, ServedResource >();
        listeners= new CopyOnWriteArrayList< Listener >();
    }

    /**
     * Add a resource to the registry. 
     * @param parent to which the resource is added as child
     * @param resource the resource to add
     */
    public void add( ServedResource parent, ServedResource resource )
    {
        //TODO responsibility of registry?
        if ( parent == null )
        {
            root.add( resource );
        }
        else
        {
            //TODO check that parent is contained in registry
            parent.add( resource );
        }
        servedResources.put( resource.getURI(), resource );
        setResourceCallBack( resource );
    }

    public void add( Listener listener )
    {
        listeners.add( listener );
        setResourceCallBack();
    }

    public void remove( ServedResource resource )
    {
        servedResources.remove( resource.getURI() );
        resource.delete();
    }

    private void setResourceCallBack()
    {
        for ( Entry< String, ServedResource > e : servedResources.entrySet() )
        {
            setResourceCallBack( e.getValue() );
        }

    }

    private void setResourceCallBack( ServedResource toServe )
    {
        Listener bestListener= null;
        int maxMatchlevel= 0;
        for ( Listener listener : listeners )
        {
            int matchLevel= matchUri( listener.getUri(), toServe.getURI() );
            if ( matchLevel > maxMatchlevel )
            {
                maxMatchlevel= matchLevel;
                bestListener= listener;
            }
        }
        if ( bestListener != null ) toServe.setCallback( bestListener.getCallback() );
    }

    public ServedResource getResource( String uri ) throws ResourceUriException
    {
        if ( uri.length() == 0 )
        {
            //do not expose root resource
            return null;
        }
        else
        {
            for ( Entry< String, ServedResource > e : servedResources.entrySet() )
            {
                if ( uri.equals( e.getKey() ) )
                {
                    return e.getValue();
                }
            }
        }
        throw new ResourceUriException( uri, ", resource does not exist." );
    }

    public List< ServedResource > findResources( String uriPattern )
    {
        ArrayList< ServedResource > found= new ArrayList< ServedResource >();

        for ( Entry< String, ServedResource > e : servedResources.entrySet() )
        {
            if ( matchUri( uriPattern, e.getKey() ) > 0 )
            {
                found.add( e.getValue() );
            }
        }
        return found;
    }

    public static int matchUri( String uriPattern, String resourceUri )
    {
        //TODO assure wildcard only occurs at end
        if ( uriHasWildcard( uriPattern ) )
        {
            if ( resourceUri.startsWith( getUriPath( uriPattern ) ) )
            {
                return getUriDepth( uriPattern );
            }
        }
        else if ( uriPattern.equals( resourceUri ) )
        {
            return Integer.MAX_VALUE;
        }
        return 0;
    }

    public static int getUriDepth( String uri )
    {
        int count;
        int index;
        for ( count= 0, index= uri.indexOf( Defs.COAP_URI_PATHSEP, 0 ); index >= 0; index= uri.indexOf( Defs.COAP_URI_PATHSEP, index + 1 ), count++ );
        return count;
    }

    public static String getUriPath( String uri )
    {
        int lastPathSep= uri.lastIndexOf( Defs.COAP_URI_PATHSEP );
        if ( lastPathSep >= 0 )
        {
            return uri.substring( 0, lastPathSep + 1 );
        }
        else
        {
            return Defs.COAP_URI_PATHSEP;
        }
    }

    public static String getUriResourceName( String uri )
    {
        int lastPathSep= uri.lastIndexOf( Defs.COAP_URI_PATHSEP );
        if ( lastPathSep >= 0 )
        {
            //TODO check uri format .*/x+
            return uri.substring( lastPathSep + 1 );
        }
        else
        {
            return Defs.COAP_URI_ROOTRESOURCE;
        }
    }

    public static boolean uriHasWildcard( String uri )
    {
        return uri.endsWith( Defs.COAP_URI_WILDCARD );
    }

    public static String getParentUri( String uri )
    {
        int lastPathSep= uri.lastIndexOf( Defs.COAP_URI_PATHSEP );
        if ( lastPathSep > 0 )
        {
            return uri.substring( 0, lastPathSep );
        }
        else
        {
            return Defs.COAP_URI_ROOTRESOURCE;
        }
    }

}
