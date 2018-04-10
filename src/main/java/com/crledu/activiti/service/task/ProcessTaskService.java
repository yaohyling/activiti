package com.crledu.activiti.service.task;

import java.util.List;

import com.crledu.activiti.domain.ProcessTaskSelector;
import com.crledu.activiti.domain.ProcessTaskVo;

public interface ProcessTaskService {

	List<ProcessTaskVo> findToDoTasks(ProcessTaskSelector selector);

	List<ProcessTaskVo> findFinishTasks(ProcessTaskSelector selector);
	
	boolean turnToDoTask(String taskId, String userId);

	boolean completeTask(String taskId, ProcessTaskSelector taskCondition);

}
