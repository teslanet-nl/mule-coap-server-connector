package nl.teslanet.mule.connectors.coap.server;

public class Resource
{
    private String path= null;
    private boolean get= true;
    private boolean post= false;
    private boolean put= false;
    private boolean sendAccept= false;



    public String getPath()
    {
        return path;
    }

    public void setPath( String path )
    {
        this.path = path;
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

    public boolean isSendAccept()
    {
        // TODO Auto-generated method stub
        return this.sendAccept;
    }
    /**
     * @param sendAccept the sendAccept to set
     */
    public void setSendAccept( boolean sendAccept )
    {
        this.sendAccept= sendAccept;
    }
}
