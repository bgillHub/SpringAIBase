package com.bgillhub.SpringAIBase.controller;

import com.bgillhub.SpringAIBase.service.BaseAiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
class BaseAiController {

    private final BaseAiService aiService;

    BaseAiController(BaseAiService aiService) {
        this.aiService = aiService;
    }

    @PostMapping("/ask")
    String ask(@RequestBody String question) {
        return aiService.getResponse(question);
    }

    @GetMapping("/item")
    BaseAiService.ItemDescription getItem() {
        return aiService.retrieveItem();
    }
}
