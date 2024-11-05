package be.pirlewiet.digitaal.web.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
					|| ( httpRequest.getRequestURI().equals( "/organisations.html" ) )
					|| ( httpRequest.getRequestURI().equals( "/registration.html" ) )
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
				
				httpResponse.sendRedirect( "/login.html" );
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
