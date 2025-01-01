package com.instape.app.rest;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.instape.app.request.RoleDto;
import com.instape.app.service.RoleService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
private final Logger logger=LogManager.getLogger(RoleController.class);
	@Autowired
    private  RoleService roleService;
	
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE.MAIN.READ')")
    public ResponseEntity<List<RoleDto>> getAllRoles(Authentication auth) {
    	logger.info("Get all roles request received");
    	List<RoleDto> roleList=roleService.getAllRoles();
    	logger.info("toatl role fetched:{}",roleList.size());
    	return new ResponseEntity<List<RoleDto>>(roleList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE.MAIN.READ')")
    public ResponseEntity<RoleDto> getRoleById(@PathVariable Long id,Authentication auth) {
    	logger.info("Get role request received, roleId:{}",id);
    	RoleDto roleDto= roleService.getRoleById(id);
    	return new ResponseEntity<RoleDto>(roleDto, HttpStatus.OK);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ROLE.MAIN.CREATE')")
    public ResponseEntity<RoleDto> createRole(@RequestBody RoleDto role,Authentication auth) {
    	logger.info("Create role request received, role name:{}",role.getRoleName());
    	Long loggedInUserId = Long.parseLong(auth.getName());
    	RoleDto roleDto= roleService.createRole(role,loggedInUserId);
    	return new ResponseEntity<RoleDto>(roleDto, HttpStatus.OK);
    }

    @PutMapping
    @PreAuthorize("hasAnyAuthority('ROLE.MAIN.CREATE')")
    public ResponseEntity<RoleDto> updateRole(@RequestBody RoleDto role,Authentication auth) {
    	logger.info("Update role request received, role name:{}",role.getRoleName());
    	Long loggedInUserId = Long.parseLong(auth.getName());
    	RoleDto roleDto= roleService.updateRole(role,loggedInUserId);
    	return new ResponseEntity<RoleDto>(roleDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ROLE.MAIN.DELETE')")
    public ResponseEntity<String> deleteRole(@PathVariable Long id,Authentication auth) {
    	logger.info("Delete role request received, roleId:{}",id);
    	Long loggedInUserId =Long.parseLong(auth.getName());
    	roleService.deleteRole(id,loggedInUserId);
        return new ResponseEntity<String>("Role deleted",HttpStatus.OK);
    }
        
    @PostMapping("/all")
    @PreAuthorize("hasAnyAuthority('ROLE.MAIN.READ')")
    public ResponseEntity<List<RoleDto>> getRole(@RequestBody Map<String,String> role,HttpServletRequest request)throws Exception {
    	logger.info("Get all roles with name only");
    	String roleType=(role==null||role.isEmpty())?"all":role.get("roleType");
    	logger.info("Requested role type: {}",roleType);
    	List<RoleDto> roleDto= roleService.getAllRoleNames(roleType);
    	logger.info("Returning role response");
    	return new ResponseEntity<List<RoleDto>>(roleDto, HttpStatus.OK);
    }
}