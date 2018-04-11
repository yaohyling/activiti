package com.crledu.activiti.service.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.stereotype.Service;

import com.crledu.activiti.constanct.ActivitiConstanct;
import com.crledu.activiti.domain.ProcessTaskSelector;
import com.crledu.activiti.domain.ProcessTaskVo;
import com.crledu.activiti.system.util.RejectActivityCMD;
import com.crledu.activiti.system.util.RejectTaskCMD;


/**
 ************************************************************
 * @CopyrightBy: 四川复兴科技有限公司
 ************************************************************
 * @Version: v0.0.1
 * @Function: 
 ************************************************************
 * @CreatedBy: yhy on 2018年4月9日上午11:17:48
 ************************************************************
 * @ModifiedBy: [name] on [time]
 * @Description:
 ************************************************************
**/
@Service
public class ProcessTaskServiceImpl implements ProcessTaskService{
	
	/**
	 * 引擎
	 */
	@Resource
	private ProcessEngine processEngine;
	
	/**
	 * activiti 任务service
	 */
	@Resource
	private TaskService taskService;
	
	@Resource
	private HistoryService historyService;
	
	@Resource
	private RepositoryService repositoryService;
	
	@Override
	public List<ProcessTaskVo> findToDoTasks(ProcessTaskSelector selector) {
//		/* 通过注入的方式获取*/
//		TaskService taskService = processEngine.getTaskService();  
		TaskQuery taskQuery = taskService.createTaskQuery();  // 获取任务查询
		List<Task> taskList = new ArrayList<>();  // 记录查询的结果
		if (selector != null) {
			String condition = selector.getProcessInstanceId();
			if (condition != null && !condition.trim().isEmpty()) {
				taskQuery = taskQuery.processInstanceId(condition);  //添加查询条件: 流程实例ID
			}
			condition = selector.getAssignee();
			if (selector.getGroup() != null && !selector.getGroup().trim().isEmpty()) {
				String[] array = selector.getGroup().split(",");
				List<String> candidateGroups = Arrays.asList(array);
				taskQuery = taskQuery.taskCandidateGroupIn(candidateGroups); //添加查询条件: 参与的组
			} else if (condition != null && !condition.trim().isEmpty()) {
				taskQuery = taskQuery.taskCandidateOrAssigned(condition); //添加查询条件: 任务参与者或直接委派人
			}
			int pageSize = selector.getPageSize(); // 每页显示条数
			int pageNum = selector.getPageNum(); // 页码
			if (pageSize != 0 ) {  // 判断是否分页
				if (pageNum == 0) { //页码默认为1
					pageNum = 1;
				}
				taskList = taskQuery.listPage((pageNum - 1)*pageSize, pageNum*pageSize - 1); // 分页查询
			}else{
				taskList = taskQuery.list(); // 不分页
			}
		}else if(selector == null){
			taskList = taskQuery.list();
		}
		List<ProcessTaskVo> taskVoList = new ArrayList<>(); 
		for (Task task : taskList) {  // 转化为值对象
			ProcessTaskVo taskVo = new ProcessTaskVo(task);
			taskVoList.add(taskVo);
		}
		return taskVoList;
	}

	@Override
	public List<ProcessTaskVo> findFinishTasks(ProcessTaskSelector selector) {
		HistoryService historyService = processEngine.getHistoryService();
		HistoricTaskInstanceQuery hisTaskQuery = historyService.createHistoricTaskInstanceQuery().finished();  // 获取历史任务query，并添加已办条件
		List<HistoricTaskInstance> taskList = new ArrayList<>();
		if (selector != null) { // 判断条件实体是否为空
			String condition = selector.getProcessInstanceId();
			if (condition != null && !condition.trim().isEmpty()) {
				hisTaskQuery = hisTaskQuery.processInstanceId(condition); //添加查询条件: 流程实例ID
			}
			condition = selector.getAssignee();
			if (selector.getGroup() != null && !selector.getGroup().trim().isEmpty()) {
				String[] array = selector.getGroup().split(",");
				List<String> candidateGroups = Arrays.asList(array);
				hisTaskQuery = hisTaskQuery.taskCandidateGroupIn(candidateGroups); //添加查询条件: 参与的组
			} else if (condition != null && !condition.trim().isEmpty()) {
				hisTaskQuery = hisTaskQuery.taskAssignee(condition);  //添加查询条件: 任务直接委派人
			}
			int pageSize = selector.getPageSize(); // 每页显示条数
			int pageNum = selector.getPageNum(); // 页码
			if (pageSize != 0 ) {  // 判断是否分页
				if (pageNum == 0) { //页码默认为1
					pageNum = 1;
				}
				taskList  = hisTaskQuery.listPage((pageNum - 1)*pageSize, pageNum*pageSize - 1); // 分页查询
			}else{
				taskList = hisTaskQuery.list(); // 不分页
			}
		}else if(selector == null){
			taskList = hisTaskQuery.list();
		}
		List<ProcessTaskVo> taskVoList = new ArrayList<>();
		for (HistoricTaskInstance historicTaskInstance : taskList) { // 转化为值对象
			ProcessTaskVo taskVo = new ProcessTaskVo(historicTaskInstance);
			taskVoList.add(taskVo);
		}
		return taskVoList;
	}

	@Override
	public boolean turnToDoTask(String taskId, String userId) {
		try {
//			TaskService taskService = processEngine.getTaskService(); // 获取任务serivce
			if (userId != null && !userId.trim().isEmpty()) {
				taskService.setAssignee(taskId, userId);
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public boolean completeTask(String taskId, ProcessTaskSelector taskCondition) {
		try {
//			TaskService taskService = processEngine.getTaskService(); // 获取任务serivce
			Task task = taskService.createTaskQuery().taskId(taskId).singleResult(); // 获取当前任务
			if (task.getAssignee() == null || task.getAssignee().trim().isEmpty()) { // 判断任务是否有直接委派人
				taskService.setAssignee(taskId, "yhy"); // taskCondition.getCurrentAccount()，否则添加当前登录账号为直接委派人
			}
			Map<String, Object> variables = new HashMap<>();
			variables.put("userId", taskCondition.getAssignee());
			variables.put("group", taskCondition.getGroup());
			variables.put("results", "0");
			taskService.complete(taskId, variables); // 通过任务ID，完成任务
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	@Override
	public String completeTaskByTaskID(String currentTaskID, Map<String, Object> processVariables, Map<String, Object> taskLocalVariables) {
		Task task = taskService.createTaskQuery().taskId(currentTaskID).singleResult(); // 获取当前任务
		if (task.getAssignee() == null || task.getAssignee().trim().isEmpty()) { // 判断任务是否有直接委派人
			taskService.setAssignee(currentTaskID, "yhy"); // taskLocalVariables.get("currentAccount");，否则添加当前登录账号为直接委派人
		}
		taskService.complete(currentTaskID, taskLocalVariables); // 通过任务ID，完成任务
		Task nextTask = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).singleResult(); // 获取当前任务
		return nextTask.getId();
	}

	@Override
	public String rejectTask(String currentTaskId, String destinationTaskId, String reason) {
		// 获取目标任务信息
		HistoricTaskInstance hisDestTask = historyService
				.createHistoricTaskInstanceQuery()
				.taskId(destinationTaskId)
				.includeTaskLocalVariables() //包含任务变量
				.singleResult();
		// 获取当前任务信息
		HistoricTaskInstance hisCurrentTask = historyService
				.createHistoricTaskInstanceQuery()
				.taskId(currentTaskId)
				.includeTaskLocalVariables()
				.singleResult();
		
		String instanceId = hisCurrentTask.getProcessInstanceId(); // 获取当前流程实例ID
		// 通过流程实例ID,获取历史任务list
		List<HistoricTaskInstance> hisTaskList = historyService
				.createHistoricTaskInstanceQuery()
				.processInstanceId(instanceId)
				.includeTaskLocalVariables() //任务变量
				.orderByTaskCreateTime()
				.asc()
				.list();
		List<HistoricTaskInstance> delHisTaskList = new ArrayList<>(); //定义一个储存需要删除的历史任务list
		Integer destinationTaskIdInt = Integer.valueOf(destinationTaskId);
		// 遍历历史任务，如果在目标任务前面的历史任务全部删除
		for (HistoricTaskInstance historicTaskInstance : hisTaskList) {
			Integer historicTaskId = Integer.valueOf(historicTaskInstance.getId());
			if (historicTaskId >= destinationTaskIdInt) {
				delHisTaskList.add(historicTaskInstance);
			}
		}
		//通过流程实例ID,获取历史
		List<HistoricActivityInstance> hisAvtivityList = historyService
				.createHistoricActivityInstanceQuery()
				.processInstanceId(instanceId)
				.orderByHistoricActivityInstanceStartTime()
	            .asc()
				.list();
		List<HistoricActivityInstanceEntity> oldHisActivityList = new ArrayList<>(); //定义一个需要储存需要恢复的历史节点list
		// 遍历历史节点， 如果节点所对应的任务ID  小于等于  目标任务ID 则删除
		for (HistoricActivityInstance historicActivityInstance : hisAvtivityList) {
			// 有些节点，任务ID为空，开始节点，结束节点。。。
			if (historicActivityInstance.getTaskId() != null) {
				Integer historicTaskId = Integer.valueOf(historicActivityInstance.getTaskId());
				if (historicTaskId <= destinationTaskIdInt) {
					oldHisActivityList.add((HistoricActivityInstanceEntity) historicActivityInstance);
				}
			} else {
	              // ActivitiConstanct.START_EVENT = "startEvent" --> 定义的常量，表示是启动节点
                if (historicActivityInstance.getActivityType().equals(ActivitiConstanct.START_EVENT)) {
                	oldHisActivityList.add((HistoricActivityInstanceEntity) historicActivityInstance);
                }
            }
		}
		// 获取流程定义实体
		ProcessDefinitionEntity prcessDef = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(hisDestTask.getProcessDefinitionId());
		List<ActivityImpl> actitityList = prcessDef.getActivities();
		// 获取目标任务的节点信息
		ActivityImpl destActivity = prcessDef.findActivity(hisDestTask.getTaskDefinitionKey());
        // 定义一个实体用于保存正在执行的任务节点信息
        ActivityImpl currentActivity = null;
        // 定义一个转向用于保存原来的任务节点的出口信息
        PvmTransition pvmTransition = null;
        // 遍历保存正在执行的任务节点的原出口信息
        for (ActivityImpl activity : actitityList) {
          // 在历史活动节点集合中获取正在运行的活动节点
            if (hisCurrentTask.getTaskDefinitionKey().equals(activity.getId())) {
                // 保存当前节点信息
                currentActivity = activity;
                // 备份流程转向信息
                pvmTransition = activity.getOutgoingTransitions().get(0);
                // 清空当前任务节点的出口信息
                activity.getOutgoingTransitions().clear();
            }
        }
        
        processEngine.getManagementService().executeCommand(new RejectTaskCMD(hisDestTask, hisCurrentTask, destActivity));
        // 当前流程的流程变量
        Map<String, Object> taskLocalVariables = hisCurrentTask.getTaskLocalVariables();
        // 目标任务中的任务变量
        Map<String, Object> processVariables = hisDestTask.getProcessVariables();
        processVariables.put(ActivitiConstanct.SKIP_EXPRESSION, false);
        taskLocalVariables.put(ActivitiConstanct.SKIP_EXPRESSION, false);
        taskLocalVariables.put(ActivitiConstanct.REJECT_REASON, reason);
        String nextTaskId = this.completeTaskByTaskID(currentTaskId, processVariables, taskLocalVariables);
        // 清空临时转向信息
        currentActivity.getOutgoingTransitions().clear();
        // 恢复原来的走向
        currentActivity.getOutgoingTransitions().add(pvmTransition);
        // 删除历史任务  ---> 虽然不太地道，但是现在也只想到了这个方法。 historyService只提供了这个方法。
        for (HistoricTaskInstance historicTaskInstance : delHisTaskList) {
            historyService.deleteHistoricTaskInstance(historicTaskInstance.getId());
        }
        // 通过实例删除所有历史节点，恢复目标节点以前的历史节点
        processEngine.getManagementService().executeCommand(new RejectActivityCMD(instanceId, oldHisActivityList));
        // 返回下个任务的任务
		return nextTaskId;
	}
}
