package org.openbizview.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


public class CrossSiteScriptingXSSFilter implements Filter {

	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
        throws IOException, ServletException {
        chain.doFilter(new CrossSiteScriptingXSSRequestWrapper((HttpServletRequest) request), response);
    }

	public void destroy() {
	}

	public void init(FilterConfig filterConfig) {
		
	}

}
