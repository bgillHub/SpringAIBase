package com.bgillhub.SpringAIBase.controller;

import com.bgillhub.SpringAIBase.service.AiCodeService;
import com.bgillhub.SpringAIBase.service.AiDatingService;
import com.bgillhub.SpringAIBase.service.RagService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

//Base Controller for the API
//TODO: Be sure to expand this to service level api calls
@RestController
@RequestMapping("/api/ai")
class BaseAiController {

    private final AiDatingService aiDatingService;
    private final AiCodeService aiService;
    private final RagService ragService;

    BaseAiController(AiDatingService aiDatingService, AiCodeService aiService, RagService ragService) {
        this.aiService = aiService;
        this.aiDatingService = aiDatingService;
        this.ragService = ragService;
    }

    @PostMapping("/ask")
    String ask(@RequestBody String question) {
        return aiService.getResponse(question);
    }

    @PostMapping("/flirt")
    Flux<String> flirt(@RequestBody String chat) {

        //TODO: Create a context-built decision on which way to go
        boolean chance = ThreadLocalRandom.current().nextBoolean();
        if (chance)
            return aiDatingService.flirt(chat);
        else {
            System.out.println("Negging");
            return aiDatingService.neg(chat);
        }
    }

    @PostMapping("/interest")
    Flux<String> interests(@RequestBody String question) {
        return ragService.getInterests(question);
    }

    //Auto Generate a profile, because bots can be trusted for dating
    @GetMapping("/profile")
    AiDatingService.ItemDescription getItem(@RequestParam List<String> hobbies, @RequestParam(required = false) List<String> movies, @RequestParam(required = false) List<String> cities) {
        return aiDatingService.retrievePositive(hobbies, movies);
    }

    @GetMapping("/profileChaos")
    List<AiDatingService.ItemDescription> getMultipleItem(@RequestParam List<String> hobbies, @RequestParam(required = false) List<String> movies, @RequestParam(required = false) List<String> cities) {
        List<AiDatingService.ItemDescription> result = new ArrayList<>();
        result.add(aiDatingService.retrievePositive(hobbies, movies));
        result.add(aiDatingService.retrieveNegative(hobbies, movies));
        return result;
    }
}
