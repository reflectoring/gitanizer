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
        registry.addResourceHandler("/static/bootstrap/**").addResourceLocations("classpath:/bootstrap/");
        registry.addResourceHandler("/static/AdminLTE/**").addResourceLocations("classpath:/AdminLTE/");
        registry.addResourceHandler("/static/jQuery/**").addResourceLocations("classpath:/jQuery/");
        registry.addResourceHandler("/static/templates/**").addResourceLocations("classpath:/templates/");
        registry.addResourceHandler("/static/codemirror/**").addResourceLocations("classpath:/codemirror/");
        registry.addResourceHandler("/static/gitanizer/**").addResourceLocations("classpath:/gitanizer/");
        registry.addResourceHandler("/static/clipboardjs/**").addResourceLocations("classpath:/clipboardjs/");
        registry.addResourceHandler("/static/img/**").addResourceLocations("classpath:/img/");
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
