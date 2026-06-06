package com.myatos.net.mcp;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "eureka.client.enabled=false"
})
class McpServerApplicationTests {

    @Test
    void contextLoads() {
    }
}
