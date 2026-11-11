package com.bgillhub.SpringAIBase.controller;

import com.bgillhub.SpringAIBase.service.BaseAiService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

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

    //Auto Generate a profile, because bots can be trusted for dating
    @GetMapping("/profile")
    BaseAiService.ItemDescription getItem(@RequestParam List<String> hobbies, @RequestParam(required = false) List<String> movies, @RequestParam(required = false) List<String> cities) {
        return aiService.retrievePositive(hobbies, movies);
    }

    @GetMapping("/profileChaos")
    List<BaseAiService.ItemDescription> getMultipleItem(@RequestParam List<String> hobbies, @RequestParam(required = false) List<String> movies, @RequestParam(required = false) List<String> cities) {
        List<BaseAiService.ItemDescription> result = new ArrayList<>();
        result.add(aiService.retrievePositive(hobbies, movies));
        result.add(aiService.retrieveNegative(hobbies, movies));
        return result;
    }
}
