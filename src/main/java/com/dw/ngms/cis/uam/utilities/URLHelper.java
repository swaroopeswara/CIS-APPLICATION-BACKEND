package com.dw.ngms.cis.uam.utilities;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class URLHelper {

	public String getUrl(HttpServletRequest request) {
	    HttpRequest httpRequest = new ServletServerHttpRequest(request);
	    UriComponents uriComponents = UriComponentsBuilder.fromHttpRequest(httpRequest).build();

	    String scheme = uriComponents.getScheme();             // http / https
	    String serverName = request.getServerName();     // hostname.com
	    int serverPort = request.getServerPort();        // 80
	    String contextPath = request.getContextPath();   // /app

	    // Reconstruct original requesting URL
	    StringBuilder url = new StringBuilder();
	    url.append(scheme).append("://");
	    url.append(serverName);

	    if (serverPort != 80 && serverPort != 443) {
	        url.append(":").append(serverPort);
	    }
	    url.append(contextPath);
	    return url.toString();
	}//getUrl
}
