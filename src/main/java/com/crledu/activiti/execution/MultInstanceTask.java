package com.crledu.activiti.execution;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.crledu.activiti.domain.ProcessTaskSelector;
import com.crledu.activiti.domain.ProcessTaskVo;
import com.crledu.activiti.service.task.ProcessTaskService;
import com.crledu.activiti.system.util.SpringBeanUtil;
import com.crledu.system.response.page.PageResponse;

public class MultInstanceTask implements ExecutionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2299558647638739692L;
	private ProcessTaskService tasksService;
	
	
	public MultInstanceTask() {
		this.tasksService = (ProcessTaskService) SpringBeanUtil.getObjectByClass(ProcessTaskService.class);
	}



	@Override
	public void notify(DelegateExecution execution) throws Exception {
		ProcessTaskSelector selector = new ProcessTaskSelector(execution.getProcessInstanceId(), null);
		PageResponse<ProcessTaskVo> xx = tasksService.findToDoTasks(selector );
		System.out.println(xx);
	}
		
}
