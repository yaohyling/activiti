package com.crledu.activiti.service.processins;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.io.FileSystemUtils;
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
	
	@Override
	public ProcessInstancessVo startInstanceByKey(String proDefKey, Map<String, Object> variables) {
		try {
			RuntimeService runtimeService = processEngine.getRuntimeService();
			ProcessInstance instance = runtimeService.startProcessInstanceByKey(proDefKey, variables);
			ProcessInstancessVo instanceVo = new ProcessInstancessVo(instance);
			return instanceVo;
		} catch (Exception e) {
			LOGGER.error("启动流程实例失败", e);
			return null;
		}
	}

}
