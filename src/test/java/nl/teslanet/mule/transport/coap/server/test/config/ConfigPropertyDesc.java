package nl.teslanet.mule.transport.coap.server.test.config;


import nl.teslanet.mule.transport.coap.server.test.config.Properties.PropertyName;


public class ConfigPropertyDesc
{

    /**
     * Name of the property
     */
    private PropertyName propertyName;

    /**
     * Californium NetworkConfig name of the property
     */
    private String propertyNetworkName;

    /**
     * Expected default value of the property 
     */
    private String expectedDefaultValue;

    /**
     * Expected default Californium NetworkConfig value
     */
    private String expectedDefaultNetworkValue;

    /**
     * Custom value to test
     */
    private String customValue;

    /**
     *  Expected Custom Californium NetworkConfig value
     */
    private String expectedCustomNetworkValue;

    /**
     * Constructor
     * @param propertyName
     * @param defaultValue
     * @param customValue
     */
    public ConfigPropertyDesc(
        Properties.PropertyName propertyName,
        String propertyNetworkName,
        String expectedDefaultValue,
        String expectedDefaultNetworkValue,
        String customValue,
        String expectedCustomNetworkValue )
    {
        this.propertyName= propertyName;
        this.propertyNetworkName= propertyNetworkName;
        this.expectedDefaultValue= expectedDefaultValue;
        this.expectedDefaultNetworkValue= expectedDefaultNetworkValue;
        this.customValue= customValue;
        this.expectedCustomNetworkValue= expectedCustomNetworkValue;
    }

    /**
     * @return the propertyName
     */
    public PropertyName getPropertyName()
    {
        return propertyName;
    }

    /**
     * @param propertyName the propertyName to set
     */
    public void setPropertyName( PropertyName propertyName )
    {
        this.propertyName= propertyName;
    }

    /**
     * @return the propertyNetworkName
     */
    public String getPropertyNetworkName()
    {
        return propertyNetworkName;
    }

    /**
     * @param propertyNetworkName the propertyNetworkName to set
     */
    public void setPropertyNetworkName( String propertyNetworkName )
    {
        this.propertyNetworkName= propertyNetworkName;
    }

    /**
     * @return the expectedDefaultValue
     */
    public String getExpectedDefaultValue()
    {
        return expectedDefaultValue;
    }

    /**
     * @param expectedDefaultValue the expectedDefaultValue to set
     */
    public void setExpectedDefaultValue( String expectedDefaultValue )
    {
        this.expectedDefaultValue= expectedDefaultValue;
    }

    /**
     * @return the expectedDefaultNetworkValue
     */
    public String getExpectedDefaultNetworkValue()
    {
        return expectedDefaultNetworkValue;
    }

    /**
     * @return the expectedCustomNetworkValue
     */
    public String getExpectedCustomNetworkValue()
    {
        return expectedCustomNetworkValue;
    }

    /**
     * @param expectedDefaultNetworkValue the expectedDefaultNetworkValue to set
     */
    public void setExpectedDefaultNetworkValue( String expectedDefaultNetworkValue )
    {
        this.expectedDefaultNetworkValue= expectedDefaultNetworkValue;
    }

    /**
     * @return the customValue
     */
    public String getCustomValue()
    {
        return customValue;
    }

    /**
     * @param customValue the customValue to set
     */
    public void setCustomValue( String customValue )
    {
        this.customValue= customValue;
    }

    /**
     * @param expectedCustomNetworkValue the expectedCustomNetworkValue to set
     */
    public void setExpectedCustomNetworkValue( String expectedCustomNetworkValue )
    {
        this.expectedCustomNetworkValue= expectedCustomNetworkValue;
    }
}
