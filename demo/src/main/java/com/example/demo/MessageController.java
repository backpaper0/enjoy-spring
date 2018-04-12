package com.example.demo;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/messages")
public class MessageController {

    private final MessageRepository repository;

    public MessageController(MessageRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public ModelAndView getAllMessage() {
        final String viewName = "message-page";
        final String modelName = "messages";
        final List<Message> modelObject = repository.findAll();
        return new ModelAndView(viewName, modelName, modelObject);
    }

    @PostMapping
    public String post(@RequestParam String content) {
        final Message entity = new Message();
        entity.setContent(content);
        repository.save(entity);
        return "redirect:/messages";
    }
}
