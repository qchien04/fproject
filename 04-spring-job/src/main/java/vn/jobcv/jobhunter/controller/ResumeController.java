package vn.jobcv.jobhunter.controller;

import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;
import jakarta.validation.Valid;
import vn.jobcv.jobhunter.domain.Company;
import vn.jobcv.jobhunter.domain.Job;
import vn.jobcv.jobhunter.domain.Resume;
import vn.jobcv.jobhunter.domain.User;
import vn.jobcv.jobhunter.domain.response.ResultPaginationDTO;
import vn.jobcv.jobhunter.domain.response.resume.ResCreateResumeDTO;
import vn.jobcv.jobhunter.domain.response.resume.ResFetchResumeDTO;
import vn.jobcv.jobhunter.domain.response.resume.ResUpdateResumeDTO;
import vn.jobcv.jobhunter.service.ResumeService;
import vn.jobcv.jobhunter.service.UserService;
import vn.jobcv.jobhunter.util.SecurityUtil;
import vn.jobcv.jobhunter.util.annotation.ApiMessage;
import vn.jobcv.jobhunter.util.error.IdInvalidException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class ResumeController {

    private final ResumeService resumeService;
    private final UserService userService;

    private final FilterBuilder filterBuilder;
    private final FilterSpecificationConverter filterSpecificationConverter;

    public ResumeController(
            ResumeService resumeService,
            UserService userService,
            FilterBuilder filterBuilder,
            FilterSpecificationConverter filterSpecificationConverter) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.filterBuilder = filterBuilder;
        this.filterSpecificationConverter = filterSpecificationConverter;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create a resume")
    public ResponseEntity<ResCreateResumeDTO> create(@Valid @RequestBody Resume resume) throws IdInvalidException {
        log.info("Creating a new resume: {}", resume);
        boolean isIdExist = this.resumeService.checkResumeExistByUserAndJob(resume);
        if (!isIdExist) {
            log.warn("User ID or Job ID does not exist: {}", resume);
            throw new IdInvalidException("User id/Job id không tồn tại");
        }
        ResCreateResumeDTO createdResume = this.resumeService.create(resume);
        log.info("Resume created successfully: {}", createdResume);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdResume);
    }

    @PutMapping("/resumes")
    @ApiMessage("Update a resume")
    public ResponseEntity<ResUpdateResumeDTO> update(@RequestBody Resume resume) throws IdInvalidException {
        log.info("Updating resume with ID: {}", resume.getId());
        Optional<Resume> reqResumeOptional = this.resumeService.fetchById(resume.getId());
        if (reqResumeOptional.isEmpty()) {
            log.warn("Resume not found with ID: {}", resume.getId());
            throw new IdInvalidException("Resume với id = " + resume.getId() + " không tồn tại");
        }
        Resume reqResume = reqResumeOptional.get();
        reqResume.setStatus(resume.getStatus());
        ResUpdateResumeDTO updatedResume = this.resumeService.update(reqResume);
        log.info("Resume updated successfully: {}", updatedResume);
        return ResponseEntity.ok().body(updatedResume);
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete a resume by id")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        log.info("Deleting resume with ID: {}", id);
        Optional<Resume> reqResumeOptional = this.resumeService.fetchById(id);
        if (reqResumeOptional.isEmpty()) {
            log.warn("Resume not found with ID: {}", id);
            throw new IdInvalidException("Resume với id = " + id + " không tồn tại");
        }
        this.resumeService.delete(id);
        log.info("Resume deleted successfully with ID: {}", id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Fetch a resume by id")
    public ResponseEntity<ResFetchResumeDTO> fetchById(@PathVariable("id") long id) throws IdInvalidException {
        log.info("Fetching resume with ID: {}", id);
        Optional<Resume> reqResumeOptional = this.resumeService.fetchById(id);
        if (reqResumeOptional.isEmpty()) {
            log.warn("Resume not found with ID: {}", id);
            throw new IdInvalidException("Resume với id = " + id + " không tồn tại");
        }
        ResFetchResumeDTO resumeDTO = this.resumeService.getResume(reqResumeOptional.get());
        log.info("Resume fetched successfully: {}", resumeDTO);
        return ResponseEntity.ok().body(resumeDTO);
    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch all resume with paginate")
    public ResponseEntity<ResultPaginationDTO> fetchAll(@Filter Specification<Resume> spec, Pageable pageable) {
        log.info("Fetching all resumes with pagination and filters");
        ResultPaginationDTO result = this.resumeService.fetchAllResume(spec, pageable);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/resumes/by-user")
    @ApiMessage("Get list resumes by user")
    public ResponseEntity<ResultPaginationDTO> fetchResumeByUser(Pageable pageable) {
        log.info("Fetching resumes by user with pagination");
        ResultPaginationDTO result = this.resumeService.fetchResumeByUser(pageable);
        return ResponseEntity.ok().body(result);
    }
}
