package com.instape.app.request;

import lombok.Data;

@Data
public class PermissionDTO {	
   
	private Long id;
	
    private String permissionName;
  
    private String permissionText;

    private String permissionGroup;
	
	
}
