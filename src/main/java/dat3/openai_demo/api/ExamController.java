package dat3.openai_demo.api;

import dat3.openai_demo.dtos.MyResponse;
import dat3.openai_demo.service.OpenAiService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/exam")
@CrossOrigin(origins = "*")
public class ExamController {

  private OpenAiService service;
  final static String SYSTEM_MESSAGE = "You are an AI that rephrases general knowledge exam questions and then answers them too. Apply these rules:\n" +

          "Only rephrase and question general theoretical questions.\n" +

          "Skip personal or project-specific details with a brief omission note.\n" +

          "Use this format for your questions and answers:\n" +

          "Question<number>: \"Rephrased general question.\"\n" +
          "Answer<number>: \"Brief answer to the question.\"\n" +

          "Example:\n" +

          "User: \"Explain the DNS System, DNS Servers and relevant DNS Records\"\n" +

          "Response:\n" +

          "Question1: \"Describe DNS and why we use it as developers.\"\n" +
          "Answer1: \"DNS, or Domain Name System, is like the internet's phonebook; it translates human-readable domain names (like 'www.example.com') into machine-readable IP addresses (like 192.0.2.1). Developers use DNS to ensure users can easily access websites with memorable names, manage multiple subdomains for different environments\n" +

          "(Note: Specific project-related parts are omitted.)\n" +

          "Respond to up to two suitable questions per input.\n" +

          "No use of back ticks ` `";

  public ExamController(OpenAiService service) {
    this.service = service;
  }

  @GetMapping
  public MyResponse getJoke(@RequestParam String about) {

    return service.makeRequest(about,SYSTEM_MESSAGE);
  }
}
