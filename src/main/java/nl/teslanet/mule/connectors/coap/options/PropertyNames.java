package nl.teslanet.mule.connectors.coap.options;

import java.util.regex.Pattern;

public class PropertyNames
{
    //request
    public static final String COAP_REQUEST_CODE= "coap.request.code";
    public static final String COAP_REQUEST_ADDRESS= "coap.request.adress";
    public static final String COAP_REQUEST_URI= "coap.request.uri";
    public static final String COAP_REQUEST_RELATION= "coap.request.relation";
    public static final String COAP_REQUEST_SOURCE_HOST= "coap.request.source.host";
    public static final String COAP_REQUEST_SOURCE_PORT= "coap.request.source.port";

    //response
    public static final String COAP_RESPONSE_CODE= "coap.response.code";

    //options
    public static final String COAP_OPT_IFMATCH_LIST= "coap.opt.if_match.list";
    public static final String COAP_OPT_URIHOST= "coap.opt.uri_host";
    public static final String COAP_OPT_ETAG_LIST= "coap.opt.etag.list";
    public static final String COAP_OPT_IFNONMATCH= "coap.opt.if_none_match";
    public static final String COAP_OPT_URIPORT= "coap.opt.uri_port";
    public static final String COAP_OPT_LOCATIONPATH_LIST= "coap.opt.location_path.list";
    public static final String COAP_OPT_LOCATIONPATH= "coap.opt.location_path";
    public static final String COAP_OPT_URIPATH_LIST= "coap.opt.uri_path.list";
    public static final String COAP_OPT_URIPATH= "coap.opt.uri_path";
    public static final String COAP_OPT_CONTENTFORMAT= "coap.opt.content_format";
    public static final String COAP_OPT_MAXAGE= "coap.opt.max_age";
    public static final String COAP_OPT_URIQUERY_LIST= "coap.opt.uri_query.list";
    public static final String COAP_OPT_URIQUERY= "coap.opt.uri_query";
    public static final String COAP_OPT_ACCEPT= "coap.opt.accept";
    public static final String COAP_OPT_LOCATIONQUERY_LIST= "coap.opt.location_query.list";
    public static final String COAP_OPT_LOCATIONQUERY= "coap.opt.location_query";
    public static final String COAP_OPT_PROXYURI= "coap.opt.proxy_uri";
    public static final String COAP_OPT_PROXYSCHEME= "coap.opt.proxy_scheme";
    public static final String COAP_OPT_BLOCK1_SZX= "coap.opt.block1.szx";
    public static final String COAP_OPT_BLOCK1_SIZE= "coap.opt.block1.size";
    public static final String COAP_OPT_BLOCK1_NUM= "coap.opt.block1.num";
    public static final String COAP_OPT_BLOCK1_M= "coap.opt.block1.m";
    public static final String COAP_OPT_BLOCK2_SZX= "coap.opt.block2.szx";
    public static final String COAP_OPT_BLOCK2_SIZE= "coap.opt.block2.size";
    public static final String COAP_OPT_BLOCK2_NUM= "coap.opt.block2.num";
    public static final String COAP_OPT_BLOCK2_M= "coap.opt.block2.m";
    public static final String COAP_OPT_SIZE1= "coap.opt.size1";
    public static final String COAP_OPT_SIZE2= "coap.opt.size2";
    public static final String COAP_OPT_OBSERVE= "coap.opt.observe";
    public static final String PREFIX_COAP_OPT_OTHER= "coap.opt.other.";   
    public static final String POSTFIX_CRITICAL=  ".critical";
    public static final String POSTFIX_NOCACHEKEY= ".no_cache_key";
    public static final String POSTFIX_UNSAFE= ".unsafe";

    public static final Pattern otherPattern= Pattern.compile( "^" + PREFIX_COAP_OPT_OTHER + "([0-9]+)" );
    
    protected PropertyNames()
    {
        
    }
}
