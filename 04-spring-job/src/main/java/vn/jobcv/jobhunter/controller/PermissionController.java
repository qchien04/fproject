package vn.jobcv.jobhunter.controller;

import lombok.extern.slf4j.Slf4j;
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
import vn.jobcv.jobhunter.domain.Permission;
import vn.jobcv.jobhunter.domain.response.ResultPaginationDTO;
import vn.jobcv.jobhunter.service.PermissionService;
import vn.jobcv.jobhunter.util.annotation.ApiMessage;
import vn.jobcv.jobhunter.util.error.IdInvalidException;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("Create a permission")
    public ResponseEntity<Permission> create(@Valid @RequestBody Permission p) throws IdInvalidException {
        log.info("Creating a new permission: {}", p);
        if (this.permissionService.isPermissionExist(p)) {
            log.warn("Permission already exists: {}", p);
            throw new IdInvalidException("Permission đã tồn tại.");
        }
        Permission createdPermission = this.permissionService.create(p);
        log.info("Permission created successfully: {}", createdPermission);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPermission);
    }

    @PutMapping("/permissions")
    @ApiMessage("Update a permission")
    public ResponseEntity<Permission> update(@Valid @RequestBody Permission p) throws IdInvalidException {
        log.info("Updating permission with ID: {}", p.getId());
        if (this.permissionService.fetchById(p.getId()) == null) {
            log.warn("Permission not found with ID: {}", p.getId());
            throw new IdInvalidException("Permission với id = " + p.getId() + " không tồn tại.");
        }
        if (this.permissionService.isPermissionExist(p) && this.permissionService.isSameName(p)) {
            log.warn("Permission with the same attributes already exists: {}", p);
            throw new IdInvalidException("Permission đã tồn tại.");
        }
        Permission updatedPermission = this.permissionService.update(p);
        log.info("Permission updated successfully: {}", updatedPermission);
        return ResponseEntity.ok().body(updatedPermission);
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Delete a permission")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) throws IdInvalidException {
        log.info("Deleting permission with ID: {}", id);
        if (this.permissionService.fetchById(id) == null) {
            log.warn("Permission not found with ID: {}", id);
            throw new IdInvalidException("Permission với id = " + id + " không tồn tại.");
        }
        this.permissionService.delete(id);
        log.info("Permission deleted successfully with ID: {}", id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/permissions")
    @ApiMessage("Fetch permissions")
    public ResponseEntity<ResultPaginationDTO> getPermissions(
            @Filter Specification<Permission> spec, Pageable pageable) {
        log.info("Fetching all permissions with pagination and filters");
        ResultPaginationDTO result = this.permissionService.getPermissions(spec, pageable);
        return ResponseEntity.ok(result);
    }
}
