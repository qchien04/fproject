package vn.jobcv.jobhunter.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import vn.jobcv.jobhunter.domain.Skill;
import vn.jobcv.jobhunter.domain.response.ResultPaginationDTO;
import vn.jobcv.jobhunter.service.SkillService;
import vn.jobcv.jobhunter.util.annotation.ApiMessage;
import vn.jobcv.jobhunter.util.error.IdInvalidException;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("Create a skill")
    public ResponseEntity<Skill> create(@Valid @RequestBody Skill s) throws IdInvalidException {
        log.info("Creating a new skill: {}", s);
        if (s.getName() != null && this.skillService.isNameExist(s.getName())) {
            log.warn("Skill name already exists: {}", s.getName());
            throw new IdInvalidException("Skill name = " + s.getName() + " đã tồn tại");
        }
        Skill createdSkill = this.skillService.createSkill(s);
        log.info("Skill created successfully: {}", createdSkill);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSkill);
    }

    @PutMapping("/skills")
    @ApiMessage("Update a skill")
    public ResponseEntity<Skill> update(@Valid @RequestBody Skill s) throws IdInvalidException {
        log.info("Updating skill with ID: {}", s.getId());
        Skill currentSkill = this.skillService.fetchSkillById(s.getId());
        if (currentSkill == null) {
            log.warn("Skill not found with ID: {}", s.getId());
            throw new IdInvalidException("Skill id = " + s.getId() + " không tồn tại");
        }
        if (s.getName() != null && this.skillService.isNameExist(s.getName())) {
            log.warn("Skill name already exists: {}", s.getName());
            throw new IdInvalidException("Skill name = " + s.getName() + " đã tồn tại");
        }
        currentSkill.setName(s.getName());
        Skill updatedSkill = this.skillService.updateSkill(currentSkill);
        log.info("Skill updated successfully: {}", updatedSkill);
        return ResponseEntity.ok().body(updatedSkill);
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        log.info("Deleting skill with ID: {}", id);
        Skill currentSkill = this.skillService.fetchSkillById(id);
        if (currentSkill == null) {
            log.warn("Skill not found with ID: {}", id);
            throw new IdInvalidException("Skill id = " + id + " không tồn tại");
        }
        this.skillService.deleteSkill(id);
        log.info("Skill deleted successfully with ID: {}", id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/skills")
    @ApiMessage("fetch all skills")
    public ResponseEntity<ResultPaginationDTO> getAll(@Filter Specification<Skill> spec, Pageable pageable) {
        log.info("Fetching all skills with pagination and filters");
        ResultPaginationDTO result = this.skillService.fetchAllSkills(spec, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(result);
    }
}
