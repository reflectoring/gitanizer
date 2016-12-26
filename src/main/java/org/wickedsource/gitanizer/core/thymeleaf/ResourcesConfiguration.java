package org.wickedsource.gitanizer.core.thymeleaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.wickedsource.gitanizer.core.WorkdirConfiguration;
import org.wickedsource.gitanizer.mirror.git.GitRepositoryResolver;

@Configuration
public class ResourcesConfiguration extends WebMvcConfigurerAdapter {

    private GitRepositoryResolver gitRepositoryResolver;

    private WorkdirConfiguration workdirConfiguration;

    @Autowired
    public ResourcesConfiguration(GitRepositoryResolver gitRepositoryResolver, WorkdirConfiguration workdirConfiguration) {
        this.gitRepositoryResolver = gitRepositoryResolver;
        this.workdirConfiguration = workdirConfiguration;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/bootstrap/**").addResourceLocations("classpath:/bootstrap/");
        registry.addResourceHandler("/AdminLTE/**").addResourceLocations("classpath:/AdminLTE/");
        registry.addResourceHandler("/jQuery/**").addResourceLocations("classpath:/jQuery/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
        registry.addResourceHandler("/codemirror/**").addResourceLocations("classpath:/codemirror/");
        registry.addResourceHandler("/img/**").addResourceLocations("classpath:/img/");
        registry.addResourceHandler("/git/**/*")
                .resourceChain(true)
                .addResolver(gitRepositoryResolver);
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("ValidationMessages");
        return messageSource;
    }
}
