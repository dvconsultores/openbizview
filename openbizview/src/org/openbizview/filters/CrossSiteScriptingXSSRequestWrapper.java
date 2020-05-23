package org.openbizview.filters;

import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

public class CrossSiteScriptingXSSRequestWrapper extends HttpServletRequestWrapper {

	public CrossSiteScriptingXSSRequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }



	private static Pattern[] patterns = new Pattern[]{
	        Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
	        Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	        Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	        Pattern.compile("", Pattern.CASE_INSENSITIVE),
	        Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	        Pattern.compile("", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	        Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	        Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
	        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
	        Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
	        Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
	    };
	
	
  
    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
  
        if (values == null) {
            return null;
        }
  
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = stripXSSAttack(values[i]);
        }
  
        return encodedValues;
    }
  
    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
  
        return stripXSSAttack(value);
    }
  
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return stripXSSAttack(value);
    }
    
    private String stripXSSAttack(String value) {
        if (value != null) {
            // NOTE: It's highly recommended to use the ESAPI library and uncomment the following line to
            // avoid encoded attacks.
            // value = ESAPI.encoder().canonicalize(value);
  
            // Avoid null characters
            value = value.replaceAll("\0", "");
  
            // Remove all sections that match a pattern
            for (Pattern scriptPattern : patterns){
                value = scriptPattern.matcher(value).replaceAll("");
            }
        }
        return value;
    }

}
