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
import org.activiti.engine.task.IdentityLink;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crledu.activiti.domain.ProcessTaskSelector;
import com.crledu.activiti.domain.ProcessTaskVo;
import com.crledu.activiti.system.util.ActivitiType;
import com.crledu.activiti.system.util.RejectActivityCMD;
import com.crledu.activiti.system.util.RejectTaskCMD;
import com.crledu.system.response.page.PageResponse;


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
@Transactional
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
	public PageResponse<ProcessTaskVo> findToDoTasks(ProcessTaskSelector selector) {
//		/* 通过注入的方式获取*/
//		TaskService taskService = processEngine.getTaskService();  
		TaskQuery taskQuery = taskService.createTaskQuery();  // 获取任务查询
		List<Task> taskList = new ArrayList<>();  // 记录查询的结果
		taskQuery = taskQuery.orderByTaskCreateTime().desc();
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
			if (selector.getPageSize() != null && selector.getPageSize() != 0 ) {  // 判断是否分页
				int pageSize = selector.getPageSize(); // 每页显示条数
				int pageNum = 0; // 页码
				if (selector.getPageNum() == null || selector.getPageNum() == 0) { //页码默认为1
					pageNum = 1;
				}else{
					pageNum = selector.getPageNum();
				}
				taskList  = taskQuery.listPage((pageNum - 1)*pageSize, pageSize); // 分页查询
			}else{
				taskList = taskQuery.list(); // 不分页
			}
		}else if(selector == null){
			taskList = taskQuery.list();
		}
		List<ProcessTaskVo> taskVoList = new ArrayList<>(); 
		for (Task task : taskList) {  // 转化为值对象
			StringBuilder sb2 = new StringBuilder();
			if (task.getAssignee() == null || task.getAssignee().trim().isEmpty()) {
				List<IdentityLink> candidate = taskService.getIdentityLinksForTask(task.getId());
				StringBuilder sb = new StringBuilder();
				for (IdentityLink identityLink : candidate) {
					if (identityLink.getUserId() != null) {
						sb.append(identityLink.getUserId()+",");
					}
					if (identityLink.getGroupId() != null) {
						sb2.append(identityLink.getGroupId() + ",");
					}
				}
				task.setAssignee(sb.toString());
			}
			ProcessTaskVo taskVo = new ProcessTaskVo(task);
			taskVo.setGroup(sb2.toString());
			taskVoList.add(taskVo);
		}
		PageResponse<ProcessTaskVo> pageResponse = new PageResponse<ProcessTaskVo>(taskVoList, taskQuery.count());
		return pageResponse;
	}

	@Override
	public PageResponse<ProcessTaskVo> findFinishTasks(ProcessTaskSelector selector) {
		HistoryService historyService = processEngine.getHistoryService();
		HistoricTaskInstanceQuery hisTaskQuery = historyService
				.createHistoricTaskInstanceQuery()
				.finished()
				.orderByTaskCreateTime()
				.desc();  // 获取历史任务query，并添加已办条件
		
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
			if (selector.getPageSize() != null && selector.getPageSize() != 0 ) {  // 判断是否分页
				int pageSize = selector.getPageSize(); // 每页显示条数
				int pageNum = 0; // 页码
				if (selector.getPageNum() == null || selector.getPageNum() == 0) { //页码默认为1
					pageNum = 1;
				}else{
					pageNum = selector.getPageNum();
				}
					
				taskList  = hisTaskQuery.listPage((pageNum - 1)*pageSize, pageSize); // 分页查询
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
		PageResponse<ProcessTaskVo> pageResponse = new PageResponse<ProcessTaskVo>(taskVoList, hisTaskQuery.count());
		return pageResponse;
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
	public List<ProcessTaskVo> completeTaskByTaskID(String currentTaskID, Map<String, Object> processVariables, Map<String, Object> taskLocalVariables) {
		Task task = taskService.createTaskQuery().taskId(currentTaskID).singleResult(); // 获取当前任务
		if (task == null) {
			return null;
		}
		if (task.getAssignee() == null || task.getAssignee().trim().isEmpty()) { // 判断任务是否有直接委派人
			
			taskService.setAssignee(currentTaskID, taskLocalVariables.get("currentAccount").toString()); // taskLocalVariables.get("currentAccount");，否则添加当前登录账号为直接委派人
		}
		taskService.complete(currentTaskID, taskLocalVariables); // 通过任务ID，完成任务
		List<Task> nextTask = taskService.createTaskQuery().processInstanceId(task.getProcessInstanceId()).list(); // 获取当前任务
		List<ProcessTaskVo> taskVoList = new ArrayList<>();
		for (Task taskNext : nextTask) {
			ProcessTaskVo taskVo = new ProcessTaskVo(taskNext);
			taskVoList.add(taskVo);
		}
		
		return taskVoList;
	}
	
	public boolean isPower(String currentTaskID, Map<String, Object> taskLocalVariables){
		List<IdentityLink> listPower = taskService.getIdentityLinksForTask(currentTaskID); // 获取
		String currentAccount = (String) taskLocalVariables.get("currentAccount"); //当前账号
		String group = (String) taskLocalVariables.get("group");  //所属组织code
		for (IdentityLink identityLink : listPower) {
			String processGroup = identityLink.getGroupId();
			if (processGroup != null && processGroup.trim().isEmpty()) {
				if (processGroup.equals(group)) {
					return true;
				}else {
					return false;
				}
			}
			String assignee = identityLink.getUserId();
			if (assignee != null && assignee.trim().isEmpty()) {
				if (assignee.equals(currentAccount)) {
					return true;
				}else {
					return false;
				}
			}
		}
		return false;
	}
	
	/**
	 * 
	 ******************************************
	 * @Function: 获取上一任务ID
	 * @param instanceId  实例ID
	 * @return
	 * String
	 ******************************************
	 * @CreatedBy: yhy on 2018年4月19日上午10:05:15
	 ******************************************
	 * @ModifiedBy: [name] on [time] 
	 * @Description:
	 ******************************************
	 *
	 */
	public String upTaskId (String instanceId){
		List<HistoricTaskInstance> listTask = historyService.createHistoricTaskInstanceQuery()
				.processInstanceId(instanceId)
				.orderByTaskCreateTime()
				.finished()
				.desc()
				.list();
		return listTask.get(0).getId();
	}
	
	@Override
	public List<ProcessTaskVo> rejectTask(String currentTaskId, String destinationTaskId, String reason) {
		// 获取当前任务信息
		HistoricTaskInstance hisCurrentTask = historyService
				.createHistoricTaskInstanceQuery()
				.taskId(currentTaskId)
				.includeTaskLocalVariables()
				.singleResult();
		// 判断目标任务ID是否为空
		if (destinationTaskId == null || destinationTaskId.trim().isEmpty()) {
			destinationTaskId = this.upTaskId(hisCurrentTask.getProcessInstanceId());  //设置目标任务ID为  上一任务ID
		}
		// 获取目标任务信息
		HistoricTaskInstance hisDestTask = historyService
				.createHistoricTaskInstanceQuery()
				.taskId(destinationTaskId)
				.includeTaskLocalVariables() //包含任务变量
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
	              // 定义的常量，表示是开始节点
                if (historicActivityInstance.getActivityType().equals(ActivitiType.START_EVENT)) {
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
        processVariables.put(ActivitiType.SKIP_EXPRESSION, false);
        taskLocalVariables.put(ActivitiType.SKIP_EXPRESSION, false);
        taskLocalVariables.put(ActivitiType.REJECT_REASON, reason);
        taskLocalVariables.put("userId", hisDestTask.getAssignee());
        List<ProcessTaskVo> nextTask = this.completeTaskByTaskID(currentTaskId, processVariables, taskLocalVariables);
        // 清空临时转向信息
        currentActivity.getOutgoingTransitions().clear();
        // 恢复原来的走向
        currentActivity.getOutgoingTransitions().add(pvmTransition);
        // 删除历史任务
        for (HistoricTaskInstance historicTaskInstance : delHisTaskList) {
            historyService.deleteHistoricTaskInstance(historicTaskInstance.getId());
        }
        // 通过实例删除所有历史节点，恢复目标节点以前的历史节点
        processEngine.getManagementService().executeCommand(new RejectActivityCMD(instanceId, oldHisActivityList));
        // 返回下个任务的任务
		return nextTask;
	}
	
}
