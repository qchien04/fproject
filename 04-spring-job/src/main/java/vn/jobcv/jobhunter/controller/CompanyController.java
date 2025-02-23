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
import vn.jobcv.jobhunter.domain.Company;
import vn.jobcv.jobhunter.domain.response.ResultPaginationDTO;
import vn.jobcv.jobhunter.service.CompanyService;
import vn.jobcv.jobhunter.util.annotation.ApiMessage;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class CompanyController {
    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/companies")
    public ResponseEntity<?> createCompany(@Valid @RequestBody Company reqCompany) {
        log.info("Creating company: {}", reqCompany);
        return ResponseEntity.status(HttpStatus.CREATED).body(this.companyService.handleCreateCompany(reqCompany));
    }

    @GetMapping("/companies")
    @ApiMessage("Fetch companies")
    public ResponseEntity<ResultPaginationDTO> getCompany(
            @Filter Specification<Company> spec, Pageable pageable) {
        log.info("Fetching companies with filters and pagination");
        return ResponseEntity.ok(this.companyService.handleGetCompany(spec, pageable));
    }

    @PutMapping("/companies")
    public ResponseEntity<Company> updateCompany(@Valid @RequestBody Company reqCompany) {
        log.info("Updating company: {}", reqCompany);
        Company updatedCompany = this.companyService.handleUpdateCompany(reqCompany);
        return ResponseEntity.ok(updatedCompany);
    }

    @DeleteMapping("/companies/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable("id") long id) {
        log.warn("Deleting company with ID: {}", id);
        this.companyService.handleDeleteCompany(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/companies/{id}")
    @ApiMessage("Fetch company by ID")
    public ResponseEntity<Company> fetchCompanyById(@PathVariable("id") long id) {
        log.info("Fetching company with ID: {}", id);
        Optional<Company> cOptional = this.companyService.findById(id);
        if (cOptional.isEmpty()) {
            log.error("Company with ID {} not found", id);
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(cOptional.get());
    }
}
