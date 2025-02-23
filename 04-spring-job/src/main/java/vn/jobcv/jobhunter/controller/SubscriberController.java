package vn.jobcv.jobhunter.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import vn.jobcv.jobhunter.domain.Subscriber;
import vn.jobcv.jobhunter.service.SubscriberService;
import vn.jobcv.jobhunter.util.SecurityUtil;
import vn.jobcv.jobhunter.util.annotation.ApiMessage;
import vn.jobcv.jobhunter.util.error.IdInvalidException;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @ApiMessage("Create a subscriber")
    public ResponseEntity<Subscriber> create(@Valid @RequestBody Subscriber sub) throws IdInvalidException {
        log.info("Creating a new subscriber: {}", sub);
        boolean isExist = this.subscriberService.isExistsByEmail(sub.getEmail());
        if (isExist) {
            log.warn("Subscriber email already exists: {}", sub.getEmail());
            throw new IdInvalidException("Email " + sub.getEmail() + " đã tồn tại");
        }
        Subscriber createdSubscriber = this.subscriberService.create(sub);
        log.info("Subscriber created successfully: {}", createdSubscriber);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSubscriber);
    }

    @PutMapping("/subscribers")
    @ApiMessage("Update a subscriber")
    public ResponseEntity<Subscriber> update(@RequestBody Subscriber subsRequest) throws IdInvalidException {
        log.info("Updating subscriber with ID: {}", subsRequest.getId());
        Subscriber subsDB = this.subscriberService.findById(subsRequest.getId());
        if (subsDB == null) {
            log.warn("Subscriber not found with ID: {}", subsRequest.getId());
            throw new IdInvalidException("Id " + subsRequest.getId() + " không tồn tại");
        }
        Subscriber updatedSubscriber = this.subscriberService.update(subsDB, subsRequest);
        log.info("Subscriber updated successfully: {}", updatedSubscriber);
        return ResponseEntity.ok().body(updatedSubscriber);
    }

    @PostMapping("/subscribers/skills")
    @ApiMessage("Get subscriber's skill")
    public ResponseEntity<Subscriber> getSubscribersSkill() throws IdInvalidException {
        log.info("Fetching subscriber's skills");
        String email = SecurityUtil.getCurrentUserLogin().orElse("");
        Subscriber subscriber = this.subscriberService.findByEmail(email);
        log.info("Subscriber skills fetched successfully for email: {}", email);
        return ResponseEntity.ok().body(subscriber);
    }
}
