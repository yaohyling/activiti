package com.crledu.activiti.service.task;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.history.HistoricTaskInstanceQuery;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.springframework.stereotype.Service;

import com.crledu.activiti.domain.ProcessTaskSelector;
import com.crledu.activiti.domain.ProcessTaskVo;


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
}
