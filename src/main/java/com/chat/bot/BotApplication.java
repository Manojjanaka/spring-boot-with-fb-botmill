package com.chat.bot;

import co.aurasphere.botmill.core.internal.util.ConfigurationUtils;
import co.aurasphere.botmill.fb.FbBotMillContext;
import co.aurasphere.botmill.fb.FbBotMillServlet;
import co.aurasphere.botmill.fb.api.MessengerProfileApi;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.DispatcherServletAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

/**
 * @author Manoj Janaka
 * @version 1.0
 * @since 7/4/17
 */

@SpringBootApplication
public class BotApplication extends SpringBootServletInitializer{

    private static final String FB_BOTMILL_PAGE_TOKEN = "fb.page.token";
    private static final String FB_BOTMILL_VALIDATION_TOKEN = "fb.validation.token";
    private static final Logger logger = LoggerFactory.getLogger(BotApplication.class);

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BotApplication.class);
    }

    public static void main(String[] args) {

        SpringApplication.run(BotApplication.class, args);

        ConfigurationUtils.loadConfigurationFile();

        FbBotMillContext.getInstance().setup(ConfigurationUtils.getConfiguration().getProperty(FB_BOTMILL_PAGE_TOKEN), ConfigurationUtils.getConfiguration().getProperty(FB_BOTMILL_VALIDATION_TOKEN));

        ConfigurationUtils.loadBotConfig();

        ConfigurationUtils.loadBotDefinitions();


        logger.debug("BotMill servlet started.");


        MessengerProfileApi.setGetStartedButton("get_started");
        MessengerProfileApi
                .setGreetingMessage("Hello, I'm a large scale BotMill-based bot!");
    }


    @Bean
    public ServletRegistrationBean dispatchServletRegistration() {

        ServletRegistrationBean registration = new ServletRegistrationBean(new FbBotMillServlet(), "/webhook");

        registration
                .setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);

        return registration;

    }
}
