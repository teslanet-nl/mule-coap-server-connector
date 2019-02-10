/*******************************************************************************
 * Copyright (c) 2017, 2018 (teslanet.nl) Rogier Cobben.
 * 
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v2.0
 * which accompanies this distribution.
 * 
 * The Eclipse Public License is available at
 *    http://www.eclipse.org/legal/epl-v20.html
 * 
 * Contributors:
 *    (teslanet.nl) Rogier Cobben - initial creation
 ******************************************************************************/

package nl.teslanet.mule.transport.coap.server.config;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.param.Optional;


public class ResourceConfig
{
    /**
     * Name of the resource, to be used in URI paths.
     */
    private String name= null;

    /**
     * When true Get requests are allowed on the resource.
     */
    @Placement(group= "Methods")
    private boolean get= true;

    /**
     * When true Post requests are allowed on the resource.
     */
    @Placement(group= "Methods")
    private boolean post= false;

    /**
     * When true Put requests are allowed on the resource.
     */
    @Placement(group= "Methods")
    private boolean put= false;

    /**
     * When true Delete requests are allowed on the resource.
     */
    @Placement(group= "Methods")
    private boolean delete= false;

    /**
     * When true the resource can be observed by clients.
     */
    private boolean observable= false;

    /**
     * When true an acknowledgement is immediately sent to the client, before processing the request and returning the response.
     * Use this when processing takes longer than the acknowledgment-timeout of the client.  
     */
    private boolean earlyAck= false;

    /**
     * Human readable title of the resource. 
     */
    @Optional
    private String title;

    /**
     * Comma separated list of interface descriptions that apply to the resource.
     */
    @Optional
    @FriendlyName("if")
    private String ifdesc;

    /**
     * Comma separated list of resource types that apply to the resource.
     */
    @Optional
    @FriendlyName("rt")
    private String rt;

    /**
     * Maximum size estimate of the resource.
     */
    @Optional
    @FriendlyName("sz")
    private String sz;

    /**
     * Comma separated list of content types that are available for the resource.
     * The types are specified by an integer as defined by CoAP.
     */
    @Optional
    @FriendlyName("ct")
    private String ct;


    @Configurable
    @Optional
    private List< ResourceConfig > resources;


    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name= name;
    }

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getIfdesc() {
		return ifdesc;
	}

	public void setIfdesc(String ifdesc) {
		this.ifdesc = ifdesc;
	}

	public String getRt() {
		return rt;
	}

	public void setRt(String rt) {
		this.rt = rt;
	}

	/**
     * @return the get
     */
    public boolean isGet()
    {
        return get;
    }

    /**
     * @param get the get to set
     */
    public void setGet( boolean get )
    {
        this.get= get;
    }

    /**
     * @return the post
     */
    public boolean isPost()
    {
        return post;
    }

    /**
     * @param post the post to set
     */
    public void setPost( boolean post )
    {
        this.post= post;
    }

    /**
     * @return the put flag
     */
    public boolean isPut()
    {
        return put;
    }

    /**
     * @param put the put to set
     */
    public void setPut( boolean put )
    {
        this.put= put;
    }

    /**
     * @return the delete flag
     */
    public boolean isDelete()
    {
        return delete;
    }

    /**
     * @param delete the delete flag to set
     */
    public void setDelete( boolean delete )
    {
        this.delete= delete;
    }

    /**
     * @return true when earlyAck flag is set on the resource. 
     */
    public boolean isEarlyAck()
    {
        return this.earlyAck;
    }

    /**
     * @param earlyAck set to true when processing requests takes more than a few seconds
     */
    public void setEarlyAck( boolean earlyAck )
    {
        this.earlyAck= earlyAck;
    }

    /**
     * @param observable the observe flag to set
     */
    public void setObservable( boolean observable )
    {
        this.observable= observable;
    }

    public boolean isObservable()
    {
        return this.observable;
    }

    /**
     * @return the child resources
     */
    public List< ResourceConfig > getResources()
    {
        return resources;
    }

    /**
     * @param resourceConfigs the child resources to set
     */
    public void setResources( List< ResourceConfig > resourceConfigs )
    {
        this.resources= resourceConfigs;
    }
    
    /**
     * @return the child resources or empty collection when not configured
     */
    public Collection< ResourceConfig > getResourceCollection()
    {
        if ( resources != null )
        {
            return resources;
        }
        else
        {
            return new ArrayList< ResourceConfig >();
        }
    }
    /**
     * @param resource the resources to add as child resource
     */
    public void addResource( ResourceConfig resource )
    {
        if ( resources == null )
        {
            resources= new ArrayList< ResourceConfig >();
        }
        resources.add( resource );
    }

    public String getSz()
    {
        return this.sz;
    }
    
    /**
     * @param sizeEstimate the sizeEstimate to set
     */
    public void setSz( String sizeEstimate )
    {
        this.sz= sizeEstimate;
    }

    /**
     * @return the contentType
     */
    public String getCt()
    {
        return ct;
    }

    /**
     * @param contentType the contentType to set
     */
    public void setCt( String contentType )
    {
        this.ct= contentType;
    }

}
