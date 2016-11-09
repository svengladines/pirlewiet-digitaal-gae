package be.pirlewiet.digitaal.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CookieFilter implements Filter {

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		try {
			HttpServletRequest httpRequest
				= (HttpServletRequest) request;
			
			HttpServletResponse httpResponse
				= (HttpServletResponse) response;
			
			// unauthenticated requests
			if ( ( httpRequest.getRequestURI().equals( "/api/codes" ) )
					|| ( httpRequest.getRequestURI().equals( "/api/ping" ) )
					|| ( httpRequest.getRequestURI().equals( "/api/coderequests" ) ) 
					|| ( httpRequest.getRequestURI().equals( "/organisation.html" ) )
					|| ( httpRequest.getRequestURI().equals( "/api/organisation/adres" ) )
					|| ( httpRequest.getRequestURI().equals( "/api/organisations" ) ) ) {
				chain.doFilter( httpRequest, httpResponse );
				return;
			}
			
			Cookie[] cookies
				= httpRequest.getCookies();
			
			boolean present
				= false;
			
			if ( cookies != null ){
				
				for ( Cookie cookie : cookies ) {
					
					if ( "pwtid".equals( cookie.getName() ) ) {
						present = true;
					}
					
				}
				
			}
			
			if ( ! present ) {
				
				httpResponse.sendRedirect( "/code.htm" );
				return;
				
			}
			else {
				chain.doFilter( httpRequest, httpResponse );
			}
			
			
		}
		catch( Exception e ) {
			throw new ServletException( e );
		}
		

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

}
