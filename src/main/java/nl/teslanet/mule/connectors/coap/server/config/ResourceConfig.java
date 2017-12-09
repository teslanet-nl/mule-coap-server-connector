package nl.teslanet.mule.connectors.coap.server.config;


import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.mule.api.annotations.Configurable;
import org.mule.api.annotations.display.FriendlyName;
import org.mule.api.annotations.display.Placement;
import org.mule.api.annotations.param.Optional;


public class ResourceConfig
{
    private String name= null;

    @Placement(group= "Methods")
    private boolean get= true;

    @Placement(group= "Methods")
    private boolean post= false;

    @Placement(group= "Methods")
    private boolean put= false;

    @Placement(group= "Methods")
    private boolean delete= false;

    @Placement(group= "Methods")
    private boolean observable= false;

    @Placement(group= "Methods")
    private boolean delayedResponse= false;

    @Configurable
    @Optional
    private List< ResourceConfig > resourceConfigs;

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name= name;
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
     * @return the put
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
     * @return the delete
     */
    public boolean isDelete()
    {
        return delete;
    }

    /**
     * @param delete the delete to set
     */
    public void setDelete( boolean delete )
    {
        this.delete= delete;
    }

    public boolean isDelayedResponse()
    {
        return this.delayedResponse;
    }

    /**
     * @param delayedResponse set to true when processing requests takes more than a few seconds
     */
    public void setDelayedResponse( boolean delayedResponse )
    {
        this.delayedResponse= delayedResponse;
    }

    /**
     * @param observable the observable to set
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
    public List< ResourceConfig > getResourceConfigs()
    {
        return resourceConfigs;
    }

    /**
     * @param resourceConfigs the child resources to set
     */
    public void setResourceConfigs( List< ResourceConfig > resourceConfigs )
    {
        this.resourceConfigs= resourceConfigs;
    }
    
    /**
     * @return the child resources or empty collection when not configured
     */
    public Collection< ResourceConfig > getResourceCollection()
    {
        if ( resourceConfigs != null )
        {
            return resourceConfigs;
        }
        else
        {
            return new ArrayList< ResourceConfig >();
        }
    }
    /**
     * @return the child resources or empty collection when not configured
     */
    public void addResource( ResourceConfig resource )
    {
        if ( resourceConfigs == null )
        {
            resourceConfigs= new ArrayList< ResourceConfig >();
        }
        resourceConfigs.add( resource );
    }



}
