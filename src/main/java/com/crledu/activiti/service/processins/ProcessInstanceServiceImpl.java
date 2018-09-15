package com.crledu.activiti.service.processins;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.crledu.activiti.controller.ActivitiProcessController;
import com.crledu.activiti.domain.ProcessInstancessVo;

@Service
public class ProcessInstanceServiceImpl implements ProcessInstanceService {
	
	protected static final Logger LOGGER = LoggerFactory.getLogger(ActivitiProcessController.class);
	
	/**
	 * 流程引擎
	 */
	@Resource
	private ProcessEngine processEngine;
	
	/**
	 * Activiti 运行时service
	 */
	@Resource
	private RuntimeService runtimeService;
	
	@Override
	public ProcessInstancessVo startInstanceByKey(String proDefKey, Map<String, Object> variables) {
		try {
			ProcessInstance instance = runtimeService.startProcessInstanceByKey(proDefKey, variables);
			ProcessInstancessVo instanceVo = new ProcessInstancessVo(instance);
			return instanceVo;
		} catch (Exception e) {
			LOGGER.error("启动流程实例失败", e);
			return null;
		}
	}

	@Override
	public List<ProcessInstancessVo> findRunningInstance(Set<String> processDefinitionKeys) {
		List<ProcessInstancessVo> instanceVoList = new ArrayList<>();
		ProcessInstanceQuery instanceQuery = runtimeService.createProcessInstanceQuery();
		if (processDefinitionKeys != null && !processDefinitionKeys.isEmpty()) {
			instanceQuery = instanceQuery.processDefinitionKeys(processDefinitionKeys);
		}
		List<ProcessInstance> list = instanceQuery.list();
		for (ProcessInstance processInstance : list) {
			ProcessInstancessVo instanceVo = new ProcessInstancessVo(processInstance);
			instanceVoList.add(instanceVo);
		}
		return instanceVoList;
	}

}
