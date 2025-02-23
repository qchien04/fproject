package vn.jobcv.jobhunter.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import vn.jobcv.jobhunter.domain.Role;
import vn.jobcv.jobhunter.domain.response.ResultPaginationDTO;
import vn.jobcv.jobhunter.service.RoleService;
import vn.jobcv.jobhunter.util.annotation.ApiMessage;
import vn.jobcv.jobhunter.util.error.IdInvalidException;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Create a role")
    public ResponseEntity<Role> create(@Valid @RequestBody Role r) throws IdInvalidException {
        log.info("Creating a new role: {}", r);
        if (this.roleService.existByName(r.getName())) {
            log.warn("Role already exists with name: {}", r.getName());
            throw new IdInvalidException("Role với name = " + r.getName() + " đã tồn tại");
        }
        Role createdRole = this.roleService.create(r);
        log.info("Role created successfully: {}", createdRole);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRole);
    }

    @PutMapping("/roles")
    @ApiMessage("Update a role")
    public ResponseEntity<Role> update(@Valid @RequestBody Role r) throws IdInvalidException {
        log.info("Updating role with ID: {}", r.getId());
        if (this.roleService.fetchById(r.getId()) == null) {
            log.warn("Role not found with ID: {}", r.getId());
            throw new IdInvalidException("Role với id = " + r.getId() + " không tồn tại");
        }
        Role updatedRole = this.roleService.update(r);
        log.info("Role updated successfully: {}", updatedRole);
        return ResponseEntity.ok().body(updatedRole);
    }

    @DeleteMapping("/roles/{id}")
    @ApiMessage("Delete a role")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        log.info("Deleting role with ID: {}", id);
        if (this.roleService.fetchById(id) == null) {
            log.warn("Role not found with ID: {}", id);
            throw new IdInvalidException("Role với id = " + id + " không tồn tại");
        }
        this.roleService.delete(id);
        log.info("Role deleted successfully with ID: {}", id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/roles")
    @ApiMessage("Fetch roles")
    public ResponseEntity<ResultPaginationDTO> getPermissions(@Filter Specification<Role> spec, Pageable pageable) {
        log.info("Fetching roles with pagination and filters");
        ResultPaginationDTO result = this.roleService.getRoles(spec, pageable);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/roles/{id}")
    @ApiMessage("Fetch role by id")
    public ResponseEntity<Role> getById(@PathVariable("id") long id) throws IdInvalidException {
        log.info("Fetching role with ID: {}", id);
        Role role = this.roleService.fetchById(id);
        if (role == null) {
            log.warn("Role not found with ID: {}", id);
            throw new IdInvalidException("Role với id = " + id + " không tồn tại");
        }
        log.info("Role fetched successfully: {}", role);
        return ResponseEntity.ok().body(role);
    }
}
