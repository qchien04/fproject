package vn.jobcv.jobhunter.controller;

import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import vn.jobcv.jobhunter.domain.Job;
import vn.jobcv.jobhunter.domain.response.ResultPaginationDTO;
import vn.jobcv.jobhunter.domain.response.job.ResCreateJobDTO;
import vn.jobcv.jobhunter.domain.response.job.ResUpdateJobDTO;
import vn.jobcv.jobhunter.service.JobService;
import vn.jobcv.jobhunter.util.annotation.ApiMessage;
import vn.jobcv.jobhunter.util.error.IdInvalidException;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("Create a job")
    public ResponseEntity<ResCreateJobDTO> create(@Valid @RequestBody Job job) {
        log.info("Creating job: {}", job);
        ResCreateJobDTO response = this.jobService.create(job);
        log.info("Job created successfully with ID: {}", response.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/jobs")
    @ApiMessage("Update a job")
    public ResponseEntity<ResUpdateJobDTO> update(@Valid @RequestBody Job job) throws IdInvalidException {
        log.info("Updating job with ID: {}", job.getId());
        Optional<Job> currentJob = this.jobService.fetchJobById(job.getId());
        if (!currentJob.isPresent()) {
            log.warn("Job not found with ID: {}", job.getId());
            throw new IdInvalidException("Job not found");
        }
        ResUpdateJobDTO response = this.jobService.update(job, currentJob.get());
        log.info("Job updated successfully with ID: {}", response.getId());
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete a job by id")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        log.info("Deleting job with ID: {}", id);
        Optional<Job> currentJob = this.jobService.fetchJobById(id);
        if (!currentJob.isPresent()) {
            log.warn("Job not found with ID: {}", id);
            throw new IdInvalidException("Job not found");
        }
        this.jobService.delete(id);
        log.info("Job deleted successfully with ID: {}", id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Get a job by id")
    public ResponseEntity<Job> getJob(@PathVariable("id") long id) throws IdInvalidException {
        log.info("Fetching job with ID: {}", id);
        Optional<Job> currentJob = this.jobService.fetchJobById(id);
        if (!currentJob.isPresent()) {
            log.warn("Job not found with ID: {}", id);
            throw new IdInvalidException("Job not found");
        }
        log.info("Job found: {}", currentJob.get());
        return ResponseEntity.ok().body(currentJob.get());
    }

    @GetMapping("/jobs")
    @ApiMessage("Get job with pagination")
    public ResponseEntity<ResultPaginationDTO> getAllJob(
            @Filter Specification<Job> spec,
            Pageable pageable) {
        log.info("Fetching all jobs with pagination");
        ResultPaginationDTO result = this.jobService.fetchAll(spec, pageable);
        return ResponseEntity.ok().body(result);
    }
}
