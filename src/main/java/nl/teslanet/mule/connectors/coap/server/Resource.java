package nl.teslanet.mule.connectors.coap.server;

public class Resource
{
    private String name= null;
    private boolean get= true;
    private boolean post= false;
    private boolean put= false;
    private boolean delayedResponse= false;
    private boolean observable= false;



    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
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
}
