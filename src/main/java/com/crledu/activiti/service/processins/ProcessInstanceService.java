package com.crledu.activiti.service.processins;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.crledu.activiti.domain.ProcessInstancessVo;

public interface ProcessInstanceService {
	
	/**
	 * 
	 ******************************************
	 * @Function: 通过流程定义key，启动流程实例
	 * @param proDefKey
	 * @return
	 * ProcessInstancessVo
	 ******************************************
	 * @CreatedBy: yhy on 2018年4月2日下午4:59:17
	 ******************************************
	 * @ModifiedBy: [name] on [time] 
	 * @Description:
	 ******************************************
	 *
	 */
	ProcessInstancessVo startInstanceByKey(String proDefKey, Map<String, Object> variables);

	List<ProcessInstancessVo> findRunningInstance(Set<String> processDefinitionKeys);

}
