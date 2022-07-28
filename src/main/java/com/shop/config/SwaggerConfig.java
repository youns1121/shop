package com.shop.config;

import com.fasterxml.classmate.TypeResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@RequiredArgsConstructor
public class SwaggerConfig {

    private final TypeResolver typeResolver;


    private String title;
    private String version;
    private String description;
    private String antPattern;
    private String made;

    @Bean
    public Docket api_v2() {
        version = "V2";
        made = " made by : ysmin";
        title = "Mybatis_" + version;
        description = "Mybatis Rest API_" + version + made;
        antPattern = "/" + version.toLowerCase() + "/api/**";

        return buildApiDocket(version, made, title, description, antPattern);
    }



    private Docket buildApiDocket(String version, String made, String title, String description, String antPattern){
        return new Docket(DocumentationType.SWAGGER_2)
//                .alternateTypeRules(AlternateTypeRules
//                        .newRule(typeResolver.resolve(Pageable.class), typeResolver.resolve(Page.class)))
                .select()
                .apis(RequestHandlerSelectors.any())
                .paths(PathSelectors.ant(antPattern))
                .build()
                .useDefaultResponseMessages(false)
                .groupName(version)
                .apiInfo(apiInfo(version, title, description))
                .globalResponseMessage(RequestMethod.GET, responseMessages())
                .globalResponseMessage(RequestMethod.POST, responseMessages())
                .globalResponseMessage(RequestMethod.DELETE, responseMessages())
                .globalResponseMessage(RequestMethod.PATCH, responseMessages())
                .globalResponseMessage(RequestMethod.PUT, responseMessages());
    }

    private ApiInfo apiInfo(String version, String title, String description){
        return new ApiInfoBuilder()
                .title(title)
                .description(description)
                .version(version)
                .build();
    }

    private List<ResponseMessage> responseMessages() {
        List<ResponseMessage> responseMessages = new ArrayList<>();
        responseMessages.add(new ResponseMessageBuilder()
                .code(200)
                .message("OK")
                .build());
        responseMessages.add(new ResponseMessageBuilder()
                .code(404)
                .message("Page Not Found")
                .build());
        responseMessages.add(new ResponseMessageBuilder()
                .code(500)
                .message("Internal Server Error")
                .build());

        return responseMessages;
    }
}
