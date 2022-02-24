package com.heather.cs;

import com.heather.cs.configuration.ApplicationConfiguration;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

public class WebApplication implements WebApplicationInitializer {
    @Override
    public void onStartup(ServletContext servletContext) {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.register(ApplicationConfiguration.class);

        ServletRegistration.Dynamic dispatcher =
                servletContext.addServlet("dispatcher", new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");

        addFilter(servletContext);
    }

    private void addFilter(ServletContext servletContext) {
        FilterRegistration.Dynamic encodingFilter = servletContext
                .addFilter("characterEncodingFilter", new CharacterEncodingFilter("UTF-8", true, true));
        encodingFilter.addMappingForUrlPatterns(null, false, "/*");
    }
}
