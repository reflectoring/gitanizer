package org.wickedsource.gitanizer.core.thymeleaf;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.wickedsource.gitanizer.mirror.git.PublicGitRepositoryResolver;
import org.wickedsource.gitanizer.mirror.git.SecureGitRepositoryResolver;

@Configuration
public class ResourcesConfiguration extends WebMvcConfigurerAdapter {

    private SecureGitRepositoryResolver secureGitRepositoryResolver;

    private PublicGitRepositoryResolver publicGitRepositoryResolver;

    @Autowired
    public ResourcesConfiguration(SecureGitRepositoryResolver secureGitRepositoryResolver, PublicGitRepositoryResolver publicGitRepositoryResolver) {
        this.secureGitRepositoryResolver = secureGitRepositoryResolver;
        this.publicGitRepositoryResolver = publicGitRepositoryResolver;
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
        registry.addResourceHandler("/git/secure/**/*")
                .resourceChain(true)
                .addResolver(secureGitRepositoryResolver);
        registry.addResourceHandler("/git/public/**/*")
                .resourceChain(true)
                .addResolver(publicGitRepositoryResolver);
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("ValidationMessages");
        return messageSource;
    }
}
