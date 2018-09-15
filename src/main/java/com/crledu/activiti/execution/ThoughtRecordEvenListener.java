package com.crledu.activiti.execution;

import org.activiti.engine.delegate.event.ActivitiEvent;
import org.activiti.engine.delegate.event.ActivitiEventListener;
import org.activiti.engine.delegate.event.ActivitiEventType;

import com.crledu.activiti.domain.ProcessTaskSelector;
import com.crledu.activiti.domain.ProcessTaskVo;
import com.crledu.activiti.service.task.ProcessTaskService;
import com.crledu.activiti.system.util.SpringBeanUtil;
import com.crledu.system.response.page.PageResponse;

/**
 * 
 ************************************************************
 * @CopyrightBy: 四川复兴科技有限公司
 ************************************************************
 * @Version: v0.0.1
 * @Function: 思想报告检测
 ************************************************************
 * @CreatedBy: yhy on 2018年7月24日上午11:29:16
 ************************************************************
 * @ModifiedBy: [name] on [time]
 * @Description:
 ************************************************************
*
 */
public class ThoughtRecordEvenListener implements ActivitiEventListener{
	
	private ProcessTaskService taskService;
	
	
	
	public ThoughtRecordEvenListener() {
		this.taskService = (ProcessTaskService) SpringBeanUtil.getObjectByClass(ProcessTaskService.class);
	}

	@Override
	public void onEvent(ActivitiEvent event) {
		ActivitiEventType eventType = event.getType();
		if (eventType == ActivitiEventType.JOB_EXECUTION_SUCCESS) {
			System.out.println("外景地交定金");
			String instanceId = event.getProcessInstanceId();
			ProcessTaskSelector selector = new ProcessTaskSelector(instanceId, null);
			PageResponse<ProcessTaskVo> tasks = taskService.findToDoTasks(selector );
			ProcessTaskSelector taskCondition  = new ProcessTaskSelector();
			taskCondition.setAssignee("xxx");
			taskCondition.setGroup("yyy");
			taskService.completeTask(tasks.getRows().get(0).getTaskID(), taskCondition  );
			PageResponse<ProcessTaskVo> tasks2 = taskService.findToDoTasks(selector );
		}
		System.out.println("阿萨德还群殴我和服从潘聪脾气好我佛脾气好");
	}

	@Override
	public boolean isFailOnException() {
		// TODO Auto-generated method stub
		System.out.println("okhjmdhgaw");
		return false;
	}

}
