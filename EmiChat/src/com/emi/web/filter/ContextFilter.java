package com.emi.web.filter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.emi.base.BeneformRuntime;
import com.google.common.base.Stopwatch;

public class ContextFilter implements Filter{

	private static final Logger log = LoggerFactory.getLogger(ContextFilter.class);

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		

	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filter) throws IOException, ServletException {
		
		Stopwatch stopwatch = Stopwatch.createStarted();
		log.info("-------Start-------");
        log.info("request url:{}", ((HttpServletRequest)request).getRequestURI());
		
		BeneformRuntime.setRequest((HttpServletRequest)request);
		BeneformRuntime.setResponse((HttpServletResponse)response);
		
		try {
			filter.doFilter(request, response);
		} catch (Exception e) {
			throw e;
		}finally{
			stopwatch.stop();
			long time = stopwatch.elapsed(TimeUnit.MILLISECONDS);
			log.info("cost {} ms", time);
            log.info("------- End -------");
		}
	}
	
	@Override
	public void destroy() {
		

	}
}
