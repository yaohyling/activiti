package com.crledu.activiti.service.task;

import java.util.List;
import java.util.Map;

import com.crledu.activiti.domain.ProcessTaskSelector;
import com.crledu.activiti.domain.ProcessTaskVo;
import com.crledu.system.response.page.PageResponse;

public interface ProcessTaskService {

	PageResponse<ProcessTaskVo> findToDoTasks(ProcessTaskSelector selector);

	PageResponse<ProcessTaskVo> findFinishTasks(ProcessTaskSelector selector);
	
	boolean turnToDoTask(String taskId, String userId);

	boolean completeTask(String taskId, ProcessTaskSelector taskCondition);

	ProcessTaskVo completeTaskByTaskID(String currentTaskID,
			Map<String, Object> processVariables,
			Map<String, Object> taskLocalVariables);
	
	/**
	 * 
	 ******************************************
	 * @Function: 驳回，返回下一任务ID
	 * @param currentTaskId 当前任务ID
	 * @param destinationTaskId 目标任务ID
	 * @param reason 原因
	 * @return
	 * String 下一任务ID
	 ******************************************
	 * @CreatedBy: yhy on 2018年4月11日下午5:36:43
	 ******************************************
	 * @ModifiedBy: [name] on [time] 
	 * @Description:
	 ******************************************
	 *
	 */
	ProcessTaskVo rejectTask(String currentTaskId, String destinationTaskId, String reason);

}
