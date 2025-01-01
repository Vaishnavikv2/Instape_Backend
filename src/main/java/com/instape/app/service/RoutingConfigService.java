package com.instape.app.service;

import java.util.Map;
import org.springframework.data.domain.PageRequest;
import com.instape.app.cloudsql.model.RoutingConfigDTO;

/**
 * 
 * 
 * @Author - Nagaraj
 * @CreationDate - 04-Nov-2024
 * @ModifyDate - 04-Nov-2024
 * @Desc -
 */
public interface RoutingConfigService {

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 11-Nov-2024
	 * @ModifyDate - 11-Nov-2024
	 * @Desc -
	 */
	Map<Object, Object> addRoutingConfig(RoutingConfigDTO payload);

	/**
	 * 
	 * 
	 * @param pageRequest
	 * @Author - Nagaraj
	 * @CreationDate - 11-Nov-2024
	 * @ModifyDate - 11-Nov-2024
	 * @Desc -
	 */
	Map<Object, Object> getRotingConfigs(PageRequest pageRequest);

	/**
	 * 
	 * 
	 * @Author - Nagaraj
	 * @CreationDate - 11-Nov-2024
	 * @ModifyDate - 11-Nov-2024
	 * @Desc -
	 */
	Map<Object, Object> updateRoutingConfig(RoutingConfigDTO payload);
}
