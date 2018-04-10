package com.crledu.system.util;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

/**
 * 
 ************************************************************
 * @CopyrightBy: 四川复兴科技有限公司
 ************************************************************
 * @Version: v0.0.1
 * @Function: 流程工具类
 ************************************************************
 * @CreatedBy: yhy on 2018年4月2日上午9:25:07
 ************************************************************
 * @ModifiedBy: [name] on [time]
 * @Description:
 ************************************************************
*
 */
@Component
public class WFUtil {

	private static Logger logger = Logger.getLogger(WFUtil.class);
	
	/**
	 * 流程引擎
	 */
	@Resource
	private ProcessEngine processEngine;

	/**
	 * 通过流程定义key，启动流程实例
	 */
	public ProcessInstance startInstanceByKey(String instanceByKey) {
		logger.debug("启用流程实例");
		RuntimeService runtimeService = processEngine.getRuntimeService();
		ProcessInstance instance = runtimeService.startProcessInstanceByKey(instanceByKey);
		logger.debug("已返回流程实例instance,id为:" + instance.getId());
		logger.debug("获取instance：" + instance);
		logger.debug("获取instance_id：" + instance.getId());
		logger.debug("获取instance_ProcessDefinitionId：" + instance.getProcessDefinitionId());
		logger.debug("获取instance_ProcessInstanceId：" + instance.getProcessInstanceId());
		logger.debug("获取instance_BusinessKey：" + instance.getBusinessKey());
		return instance;
	}

	/**
	 * 获取流程信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public ActivityImpl getProcessMap(String procDefId, String executionId)
			throws Exception {
		logger.debug("获取流程定义信息:" + procDefId);
		logger.debug("获取流程实例信息:" + executionId);
		RepositoryService repositoryService = processEngine
				.getRepositoryService();
		ProcessDefinition processDefinition = repositoryService
				.createProcessDefinitionQuery().processDefinitionId(procDefId)
				.singleResult();
		logger.debug("查看流程定义名称:" + processDefinition.getName());
		ProcessDefinitionImpl pdImpl = (ProcessDefinitionImpl) processDefinition;
		String processDefinitionId = pdImpl.getId();
		logger.debug("流程标识 :" + processDefinitionId);
		ProcessDefinitionEntity def = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
				.getDeployedProcessDefinition(processDefinitionId);
		ActivityImpl actImpl = null;

		RuntimeService runtimeService = processEngine.getRuntimeService();
		ExecutionEntity execution = (ExecutionEntity) runtimeService
				.createExecutionQuery().executionId(executionId).singleResult();
		// 执行实例

		String activitiId = execution.getActivityId();// 当前实例的执行到哪个节点
		logger.debug("当前执行节点id:" + activitiId);
		List<ActivityImpl> activitiList = def.getActivities();// 获得当前任务的所有节点
		logger.debug("当前任务的所有节点个数:" + activitiList.size());
		for (ActivityImpl activityImpl : activitiList) {
			String id = activityImpl.getId();
			if (id.equals(activitiId)) {// 获得执行到那个节点
				actImpl = activityImpl;
				break;
			}
		}
		logger.debug(actImpl);
		return actImpl;
	}

	/**
	 * 获取流程图并 显示
	 * 
	 * @return
	 * @throws Exception
	 */
	public InputStream findProcessPic(String procDefId) throws Exception {
		RepositoryService repositoryService = processEngine
				.getRepositoryService();
		ProcessDefinition procDef = repositoryService
				.createProcessDefinitionQuery().processDefinitionId(procDefId)
				.singleResult();
		String diagramResourceName = procDef.getDiagramResourceName();
		InputStream imageStream = repositoryService.getResourceAsStream(
				procDef.getDeploymentId(), diagramResourceName);
		return imageStream;
	}

	/**
	 * 
	 ******************************************
	 * @Function: 根据任务委托人，获取任务
	 * @param assignee
	 * @return
	 * List<Task>
	 ******************************************
	 * @CreatedBy: yhy on 2018年4月2日上午9:30:45
	 ******************************************
	 * @ModifiedBy: [name] on [time] 
	 * @Description:
	 ******************************************
	 *
	 */
	public List<Task> findTaskByAssignee(String assignee) {
		logger.debug("根据任务委托人>>"+assignee+"<<获取任务...");
		TaskService taskService = processEngine.getTaskService();
		List<Task> taskList = taskService.createTaskQuery().taskAssignee(assignee).list();
		logger.debug("任务个数:" + taskList.size());
		return taskList;
	}
	public List<Task> findTaskByAssigneeLike(String assignee) {
		logger.debug("根据任务受托人>>"+assignee+"<<模糊获取任务...");
		TaskService taskService = processEngine.getTaskService();
		List<Task> taskList = taskService.createTaskQuery().taskAssigneeLike(assignee).list();
		logger.debug("任务个数:" + taskList.size());
		return taskList;
	}

	/**
	 * 
	 ******************************************
	 * @Function: 完成任务
	 * @param taskid 任务ID
	 * void
	 ******************************************
	 * @CreatedBy: yhy on 2018年4月2日上午9:33:57
	 ******************************************
	 * @ModifiedBy: [name] on [time] 
	 * @Description:
	 ******************************************
	 *
	 */
	public boolean takeCompleteTask(String taskId, String userId) {
		if (taskId == null || taskId.trim().isEmpty()) {
			logger.debug("任务ID不能为空");
			return false;
		}
		try {
			TaskService taskService = processEngine.getTaskService();
			if (userId != null && !userId.trim().isEmpty()) {
				Map<String, Object> variables = new HashMap<String, Object>();
				variables.put(userId, userId);
				taskService.complete(taskId, variables);
			}else{
				taskService.complete(taskId);
			}
			return true;
		} catch (Exception e) {
			logger.error("完成任务失败"+e.getMessage(), e);
			return false;
		}
	}

}
