package org.keretrendszer.beadando.masterverse.web_server_config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.HiddenHttpMethodFilter;

@Configuration
public class HttpRequestConfig
{
    @Bean
    public HiddenHttpMethodFilter convertGetOrPostRequestWithHiddenMethod()
    {
        return new HiddenHttpMethodFilter();
    }
}
