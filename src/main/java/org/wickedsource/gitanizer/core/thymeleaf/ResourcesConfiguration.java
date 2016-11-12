package org.wickedsource.gitanizer.core.thymeleaf;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class ResourcesConfiguration extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/bootstrap/**").addResourceLocations("classpath:/bootstrap/");
        registry.addResourceHandler("/AdminLTE/**").addResourceLocations("classpath:/AdminLTE/");
        registry.addResourceHandler("/jQuery/**").addResourceLocations("classpath:/jQuery/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
    }
}
