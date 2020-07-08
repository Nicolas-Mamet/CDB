package com.excilys.cdb.mvc;

import java.util.List;
import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import com.excilys.cdb.mvc.controller.AddComputerController;
import com.excilys.cdb.mvc.controller.ComputerDTOResolver;
import com.excilys.cdb.mvc.controller.ControllerCommonFunction;
import com.excilys.cdb.mvc.controller.DeleteComputerController;
import com.excilys.cdb.mvc.controller.EditComputerController;
import com.excilys.cdb.mvc.controller.PrepareAddController;
import com.excilys.cdb.mvc.controller.PrepareDashboardController;
import com.excilys.cdb.mvc.controller.PrepareEditController;
import com.excilys.cdb.mvc.controller.ProblemListController;

@Configuration
@EnableWebMvc
//@ImportResource("classpath:/applicationcontext.xml")
@ComponentScan(
    basePackageClasses = { AddComputerController.class,
            ControllerCommonFunction.class, DeleteComputerController.class,
            EditComputerController.class, PrepareAddController.class,
            PrepareDashboardController.class, PrepareEditController.class,
            ProblemListController.class })
public class SpringConfigMVC implements WebMvcConfigurer {
    @Bean
    public ViewResolver getViewLocation() {
        InternalResourceViewResolver viewResolver =
                new InternalResourceViewResolver();
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/");
        viewResolver.setSuffix(".jsp");
        return viewResolver;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**")
                .addResourceLocations("/resources/")
                .addResourceLocations("/resources/js")
                .addResourceLocations("/resources/css");
    }

    @Override
    public void addArgumentResolvers(
            List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new ComputerDTOResolver());
    }

    @Bean
    public LocaleResolver localeResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.ENGLISH);
        return slr;
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    @Bean
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource =
                new ResourceBundleMessageSource();
        messageSource.setBasenames("messages");
        messageSource.setDefaultEncoding("ISO-8859-1");
        return messageSource;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
