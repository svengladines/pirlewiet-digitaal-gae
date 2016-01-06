package be.pirlewiet.registrations.web.filters;

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
			if ( ( httpRequest.getRequestURI().equals( "/rs/codes" ) )
					|| ( httpRequest.getRequestURI().equals( "/rs/ping" ) )
					|| ( httpRequest.getRequestURI().equals( "/rs/coderequests" ) ) 
					|| ( httpRequest.getRequestURI().equals( "/rs/organisation.html" ) )
					|| ( httpRequest.getRequestURI().equals( "/rs/organisation/adres" ) )
					|| ( httpRequest.getRequestURI().equals( "/rs/organisations" ) ) ) {
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
