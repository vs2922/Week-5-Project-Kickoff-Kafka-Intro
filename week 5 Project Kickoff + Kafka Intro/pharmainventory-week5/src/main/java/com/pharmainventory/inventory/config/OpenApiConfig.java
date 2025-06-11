package com.pharmainventory.inventory.config;

import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@Configuration
@OpenAPIDefinition(info = @Info(
    title = "PharmaInventory API",
    version = "v0.3.0",
    description = "Product Catalog and Inventory endpoints"
))
public class OpenApiConfig {
    // No additional beans needed; annotations drive SpringDoc setup
}
