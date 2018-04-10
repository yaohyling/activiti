package com.crledu.activiti.service.processdef;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.springframework.stereotype.Service;

import com.crledu.activiti.domain.ProcessDefSelector;
import com.crledu.activiti.domain.ProcessDefinitionVo;

@Service
public class ProcessDefinitionServiceImpl implements ProcessDefinitionService{
	
	/**
	 * 流程引擎
	 */
	@Resource
	private ProcessEngine processEngine;

	
	@Override
	public List<ProcessDefinitionVo> findDeployBySelector(ProcessDefSelector selector) {
		RepositoryService repositoryService = processEngine.getRepositoryService(); 
		ProcessDefinitionQuery pdq = repositoryService.createProcessDefinitionQuery();
		if (selector != null) {
			String condition = selector.getProcessDefId();
			if (condition != null && !condition.trim().isEmpty()) {
				pdq = pdq.processDefinitionId(condition);
			}
			condition = selector.getProcessDefKey();
			if (condition != null && !condition.trim().isEmpty()) {
				pdq = pdq.processDefinitionKey(condition);
			}
			condition = selector.getProcessDefName();
			if (condition != null && !condition.trim().isEmpty()) {
				pdq = pdq.processDefinitionName(condition);
			}
			List<ProcessDefinition> listProcess = pdq.list();
			List<ProcessDefinitionVo> list = new ArrayList<>();
			for (ProcessDefinition processDefinition : listProcess) {
				ProcessDefinitionVo myProDef = new ProcessDefinitionVo(processDefinition);
				list.add(myProDef);
			}
			return list;
		}
		return null;
	}
		
}
