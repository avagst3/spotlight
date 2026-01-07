package com.spotlight.back.spotlight.configurations.properties;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
@Validated
public class JwtProperties {

    @NotEmpty
    private String issuer;

    @NotEmpty
    private String privateKey;

    @NotEmpty
    private String publicKey;

    @NotNull
    @Min(0)
    private Integer validity;

    @NotEmpty
    private String algorithm;

}
