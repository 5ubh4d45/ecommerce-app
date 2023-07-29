package dev.ixale.ecommerceservice.config.swagger;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

@SecurityScheme(name = "noAuth", type = SecuritySchemeType.HTTP)
public class OpenApiHttpNoAuth {
}
