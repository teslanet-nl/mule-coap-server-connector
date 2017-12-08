package nl.teslanet.mule.connectors.coap.options;


import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.californium.core.coap.BlockOption;
import org.eclipse.californium.core.coap.Option;


public class OptionSet extends org.eclipse.californium.core.coap.OptionSet
{
    public OptionSet()
    {
        super();
    }

    public OptionSet( org.eclipse.californium.core.coap.OptionSet options )
    {
        super( options );
    }

    @SuppressWarnings("unchecked")
    public OptionSet( Map< String, Object > props )
    {
        super();

        for ( Entry< String, Object > e : props.entrySet() )
        {
            /*OptionSet() */
            switch ( e.getKey() )
            {
                /* if_match_list       = null; // new LinkedList<byte[]>();*/
                case PropertyNames.COAP_OPT_IFMATCH_LIST:
                    if ( Collection.class.isInstance( e.getValue() ) )
                    {
                        for ( Object val : ( (Collection< Object >) e.getValue() ) )
                        {
                            this.addIfMatch( toBytes( val ) );
                        }
                    }
                    else if ( Object.class.isInstance( e.getValue() ) )
                    {
                        this.addIfMatch( toBytes(  e.getValue() ));
                    }
                    break;
                /*uri_host            = null; // from sender */
                case PropertyNames.COAP_OPT_URIHOST:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        //TODO support for comma separated values?
                        this.setUriHost( e.getValue().toString() );
                    }
                    break;
                /*
                etag_list           = null; // new LinkedList<byte[]>();*/
                case PropertyNames.COAP_OPT_ETAG_LIST:
                    //TODO check whether multiple values is valid
                    if ( Collection.class.isInstance( e.getValue() ) )
                    {
                        for ( Object val : ( (Collection< Object >) e.getValue() ) )
                        {
                            this.addETag( toBytes( val) );
                        }
                    }
                    else if ( Object.class.isInstance( e.getValue() ) )
                    {
                        //TODO support for comma separated values?
                        this.addETag( toBytes( e.getValue()) );
                    }
                    break;
                /*if_none_match       = false; */
                case PropertyNames.COAP_OPT_IFNONMATCH:
                    this.setIfNoneMatch( toBoolean( e.getValue() ));
                    break;

                /*
                uri_port            = null; // from sender*/
                case PropertyNames.COAP_OPT_URIPORT:
                    this.setUriPort( toInteger( e.getValue() ));
                    break;
                /*
                location_path_list  = null; // new LinkedList<String>();*/
                case PropertyNames.COAP_OPT_LOCATIONPATH_LIST:
                    //TODO check for duplication with LOCATIONPATH
                    if ( Collection.class.isInstance( e.getValue() ) )
                    {
                        for ( Object val : ( (Collection< Object >) e.getValue() ) )
                        {
                            this.addLocationPath( val.toString() );
                        }
                    }
                    break;
                case PropertyNames.COAP_OPT_LOCATIONPATH:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        this.setLocationPath( e.getValue().toString() );
                    }
                    break;
                /*
                uri_path_list       = null; // new LinkedList<String>();*/
                case PropertyNames.COAP_OPT_URIPATH_LIST:
                    //TODO check for duplication with COAP_OPT_URIPATH
                    if ( Collection.class.isInstance( e.getValue() ) )
                    {
                        for ( Object val : ( (Collection< Object >) e.getValue() ) )
                        {
                            this.addUriPath( val.toString() );
                        }
                    }
                    break;
                case PropertyNames.COAP_OPT_URIPATH:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        this.setUriPath( e.getValue().toString() );
                    }
                    break;
                /*
                content_format      = null;*/
                case PropertyNames.COAP_OPT_CONTENTFORMAT:
                    //TODO add support for Content-Format?
                    //TODO add support for mime-type?
                    //TODO support for format as mime-type string
                    this.setContentFormat( toInteger( e.getValue()) );
                    break;
                /*
                max_age             = null;*/
                case PropertyNames.COAP_OPT_MAXAGE:
                    //TODO add support for Content-Format?
                    //TODO add support for mime-type?
                    //TODO support for format as string
                    this.setMaxAge( toLong( e.getValue()) );
                    break;
                /*
                uri_query_list      = null; // new LinkedList<String>();*/
                case PropertyNames.COAP_OPT_URIQUERY_LIST:
                    //TODO check for duplication with COAP_OPT_URIQUERY
                    if ( Collection.class.isInstance( e.getValue() ) )
                    {
                        for ( Object val : ( (Collection< Object >) e.getValue() ) )
                        {
                            this.addUriQuery( val.toString() );
                        }
                    }
                case PropertyNames.COAP_OPT_URIQUERY:

                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        this.setUriQuery( e.getValue().toString() );
                    }
                    break;
                /*
                accept              = null;*/
                case PropertyNames.COAP_OPT_ACCEPT:
                    //TODO add support for Content-Format?
                    //TODO add support for mime-type?
                    //TODO support for format as string
                    this.setAccept( toInteger( e.getValue()) );
                    break;
                /*
                location_query_list = null; // new LinkedList<String>();*/
                case PropertyNames.COAP_OPT_LOCATIONQUERY_LIST:
                    if ( Collection.class.isInstance( e.getValue() ) )
                    {
                        for ( Object val : ( (Collection< Object >) e.getValue() ) )
                        {
                            this.addLocationQuery( val.toString() );
                        }
                    }
                    break;
                case PropertyNames.COAP_OPT_LOCATIONQUERY:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        this.setLocationQuery( e.getValue().toString() );
                    }
                    break;
                /*
                proxy_uri           = null;*/
                case PropertyNames.COAP_OPT_PROXYURI:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        this.setProxyUri( e.getValue().toString() );
                    }
                    break;
                /*
                proxy_scheme        = null;*/
                case PropertyNames.COAP_OPT_PROXYSCHEME:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        this.setProxyScheme( e.getValue().toString() );
                    }
                    break;
                /*
                block1              = null;*/
                case PropertyNames.COAP_OPT_BLOCK1_SIZE:
                    //TODO check for duplicate with szx
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        assureBlock1Exists();
                        getBlock1().setSzx( BlockOption.size2Szx( toInteger( e.getValue() )) );
                    }
                    break;                case PropertyNames.COAP_OPT_BLOCK1_SZX:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        assureBlock1Exists();
                        getBlock1().setSzx( toInteger( e.getValue() ) );
                    }
                    break;
                case PropertyNames.COAP_OPT_BLOCK1_NUM:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        assureBlock1Exists();
                        getBlock1().setNum( toInteger( e.getValue() ) );
                    }
                    break;
                case PropertyNames.COAP_OPT_BLOCK1_M:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        assureBlock1Exists();
                        getBlock1().setM( toBoolean( e.getValue() ) );
                    }
                    break;
               /*
                block2              = null;*/
                case PropertyNames.COAP_OPT_BLOCK2_SIZE:
                    //TODO check for duplicate with szx
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        assureBlock2Exists();
                        getBlock2().setSzx( BlockOption.size2Szx( toInteger( e.getValue() )) );
                    }
                    break;
                case PropertyNames.COAP_OPT_BLOCK2_SZX:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        assureBlock2Exists();
                        getBlock2().setSzx( toInteger( e.getValue() ) );
                    }
                    break;
                case PropertyNames.COAP_OPT_BLOCK2_NUM:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        assureBlock2Exists();
                        getBlock2().setNum( toInteger( e.getValue() ) );
                    }
                    break;
                case PropertyNames.COAP_OPT_BLOCK2_M:
                    if ( Object.class.isInstance( e.getValue() ) )
                    {
                        assureBlock2Exists();
                        getBlock2().setM( toBoolean( e.getValue() ) );
                    }
                    break;
                /*
                 * size1               = null;*/
                case PropertyNames.COAP_OPT_SIZE1:
                    this.setSize1( toInteger( e.getValue() ));
                    break;
                /*
                size2               = null;*/
                case PropertyNames.COAP_OPT_SIZE2:
                    this.setSize2( toInteger( e.getValue() ));
                    break;
                /*
                observe             = null;*/
                case PropertyNames.COAP_OPT_OBSERVE:
                    this.setObserve( toInteger( e.getValue() ));
                    break;
                    
                default:
                    /*               
                    others              = null; // new LinkedList<>();
                    */
                    Integer optionNr= optionNrfromPropertyName( e.getKey() );
                    if ( optionNr >= 0 && e.getValue() != null )
                    {
                        Object o= e.getValue();
                        Option option= new Option( optionNr );
                        if ( byte[].class.isAssignableFrom( o.getClass() ) )
                        {
                            option.setValue( (byte[]) o );
                            addOption( option );
                        }
                    }
            }
        }
    }

    private void assureBlock1Exists()
    {
        if ( !hasBlock1())
        {
            setBlock1( new BlockOption());
        }
        
    }

    private void assureBlock2Exists()
    {
        if ( !hasBlock1())
        {
            setBlock2( new BlockOption());
        }
        
    }    
    
    private Long toLong( Object object )
    {
        if ( Long.class.isInstance( object ) )
        {
            return (Long) object;
        }
        else if ( Object.class.isInstance( object ) )
        {
            return Long.parseLong( object.toString()  );
        }        
        return null;
    }

    private Integer toInteger( Object object )
    {
        if ( Integer.class.isInstance( object ) )
        {
            return (Integer) object;
        }
        else if ( Object.class.isInstance( object ) )
        {
           return Integer.parseInt( object.toString() );
        }        
        return null;
    }

    private Boolean toBoolean( Object object )
    {
        if ( Boolean.class.isInstance( object ) )
        {
            return (Boolean) object;
        }
        else if ( Object.class.isInstance( object ) )
        {
            return Boolean.parseBoolean( object.toString() ) ;
        } 
        return null;
    }

    private byte[] toBytes( Object object )
    {
        if ( Object.class.isInstance( object ) )
        {
            if ( Byte[].class.isInstance( object ))
            {
                return (byte[]) object;
            }
            else
            {
                return object.toString().getBytes();
            }
        }
        return null;
    }

    public static void fillProperties( org.eclipse.californium.core.coap.OptionSet options, Map< String, Object > props )
    {
        // List<byte[]> if_match_list;
        if ( !options.getIfMatch().isEmpty())
        {
            props.put( PropertyNames.COAP_OPT_IFMATCH_LIST, options.getIfMatch() );
        }
        // String       uri_host;
        if ( options.hasUriHost())
        {
            props.put( PropertyNames.COAP_OPT_URIHOST, options.getUriHost() );
        }
        // List<byte[]> etag_list;
        if ( !options.getETags().isEmpty())
        {
            props.put( PropertyNames.COAP_OPT_ETAG_LIST, options.getETags() );
        }
        // boolean      if_none_match; // true if option is set
        props.put( PropertyNames.COAP_OPT_IFNONMATCH, Boolean.valueOf(  options.hasIfNoneMatch()) );
        
        // Integer      uri_port; // null if no port is explicitly defined
        if ( options.hasUriPort())
        {
            props.put( PropertyNames.COAP_OPT_URIPORT, options.getUriPort() );
        }        
        // List<String> location_path_list;
        if ( !options.getLocationPath().isEmpty())
        {
            props.put( PropertyNames.COAP_OPT_LOCATIONPATH_LIST, options.getLocationPath() );
            props.put( PropertyNames.COAP_OPT_LOCATIONPATH, options.getLocationPathString() );
        }        
        // List<String> uri_path_list;
        if ( !options.getUriPath().isEmpty())
        {
            props.put( PropertyNames.COAP_OPT_URIPATH_LIST, options.getUriPath() );
            props.put( PropertyNames.COAP_OPT_URIPATH, options.getUriPathString() );
        }          
        // Integer      content_format;
        if ( options.hasContentFormat())
        {
            //TODO as text?
            props.put( PropertyNames.COAP_OPT_CONTENTFORMAT, Integer.valueOf( options.getContentFormat() ));
        }    
        // Long         max_age; // (0-4 bytes)
        if ( options.hasMaxAge())
        {
            props.put( PropertyNames.COAP_OPT_MAXAGE, options.getMaxAge() );
        }          
        // List<String> uri_query_list;
        if ( !options.getUriQuery().isEmpty())
        {
            props.put( PropertyNames.COAP_OPT_URIQUERY_LIST, options.getUriQuery() );
            props.put( PropertyNames.COAP_OPT_URIQUERY, options.getUriQueryString());
        }    
        // Integer      accept;
        if ( options.hasAccept())
        {
            props.put( PropertyNames.COAP_OPT_ACCEPT, Integer.valueOf( options.getAccept()));
        }
        // List<String> location_query_list;
        if ( !options.getLocationQuery().isEmpty())
        {
            props.put( PropertyNames.COAP_OPT_LOCATIONQUERY_LIST, options.getLocationQuery() );
            props.put( PropertyNames.COAP_OPT_LOCATIONQUERY, options.getLocationQueryString());
        }          
        // String       proxy_uri;
        if ( options.hasProxyUri())
        {
            props.put( PropertyNames.COAP_OPT_PROXYURI, options.getProxyUri() );
        }           
        // String       proxy_scheme;
        if ( options.hasProxyScheme())
        {
            props.put( PropertyNames.COAP_OPT_PROXYSCHEME, options.getProxyScheme() );
        }           
        // BlockOption  block1;
        if ( options.hasBlock1())
        {
            props.put( PropertyNames.COAP_OPT_BLOCK1_SZX, Integer.valueOf( options.getBlock1().getSzx() ));
            props.put( PropertyNames.COAP_OPT_BLOCK1_SIZE, Integer.valueOf( options.getBlock1().getSize() ));
            props.put( PropertyNames.COAP_OPT_BLOCK1_NUM, Integer.valueOf( options.getBlock1().getNum()));
            props.put( PropertyNames.COAP_OPT_BLOCK1_M, Boolean.valueOf( options.getBlock1().isM()));
        }          
        // BlockOption  block2;
        if ( options.hasBlock2())
        {
            props.put( PropertyNames.COAP_OPT_BLOCK2_SZX, Integer.valueOf( options.getBlock2().getSzx() ));
            props.put( PropertyNames.COAP_OPT_BLOCK2_SIZE, Integer.valueOf( options.getBlock2().getSize() ));
            props.put( PropertyNames.COAP_OPT_BLOCK2_NUM, Integer.valueOf( options.getBlock2().getNum()));
            props.put( PropertyNames.COAP_OPT_BLOCK2_M, Boolean.valueOf( options.getBlock2().isM()));
        }          
        // Integer      size1;
        if ( options.hasSize1())
        {
            props.put( PropertyNames.COAP_OPT_SIZE1, options.getSize1());
        }
        // Integer      size2;
        if ( options.hasSize2())
        {
            props.put( PropertyNames.COAP_OPT_SIZE2, options.getSize2());
        }
        // Integer      observe;
        if ( options.hasObserve())
        {
            props.put( PropertyNames.COAP_OPT_OBSERVE, options.getObserve());
        }
        // Arbitrary options
        // List<Option> others;
        for ( Option other : options.getOthers())
        {
            props.put( PropertyNames.PREFIX_COAP_OPT_OTHER + other.getNumber(), other.getValue() );
            props.put( PropertyNames.PREFIX_COAP_OPT_OTHER + other.getNumber() + PropertyNames.POSTFIX_CRITICAL, Boolean.valueOf( other.isCritical() ));
            props.put( PropertyNames.PREFIX_COAP_OPT_OTHER + other.getNumber() + PropertyNames.POSTFIX_NOCACHEKEY, Boolean.valueOf( other.isNoCacheKey() ));
            props.put( PropertyNames.PREFIX_COAP_OPT_OTHER + other.getNumber() + PropertyNames.POSTFIX_UNSAFE, Boolean.valueOf( other.isUnSafe() ));
        }
    }

    private int optionNrfromPropertyName( String propertyName )
    {
        Matcher m= PropertyNames.otherPattern.matcher( propertyName );

        // if an occurrence if a pattern was found in a given string...
        if ( m.find() )
        {
            return Integer.parseInt( m.group( 0 ) );

        }
        else
        {
            return -1;
        }
    }

}