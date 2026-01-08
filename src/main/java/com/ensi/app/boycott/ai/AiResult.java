package com.ensi.app.boycott.ai;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AiResult {
    private String productName;
    private boolean boycotted;
    private String alternative;
    private String reason;
    private String source;
    private String llmResponse;
}

