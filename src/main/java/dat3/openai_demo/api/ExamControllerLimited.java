package dat3.openai_demo.api;

import dat3.openai_demo.dtos.MyResponse;
import dat3.openai_demo.service.OpenAiService;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/api/exam2")
@CrossOrigin(origins = "*")
public class ExamControllerLimited {

  private final int BUCKET_CAPACITY = 10;
  private final int REFILL_AMOUNT = 10;
  private final int REFILL_TIME = 2;

  private OpenAiService service;

  private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

  public ExamControllerLimited(OpenAiService service) {
    this.service=service;
  }

  private Bucket createNewBucket() {
    Bandwidth limit = Bandwidth.classic(BUCKET_CAPACITY, Refill.greedy(REFILL_AMOUNT, Duration.ofMinutes(REFILL_TIME)));
    return Bucket.builder().addLimit(limit).build();
  }

  private Bucket getBucket(String key) {
    return buckets.computeIfAbsent(key, k -> createNewBucket());
  }

  @GetMapping("/limited")
  public MyResponse getJokeLimited(@RequestParam String about, HttpServletRequest request) {
    String ip = request.getRemoteAddr();
    Bucket bucket = getBucket(ip);
    if (!bucket.tryConsume(1)) {
      throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests, try again later");
    }
    return service.makeRequest(about, ExamController.SYSTEM_MESSAGE);
  }
}

