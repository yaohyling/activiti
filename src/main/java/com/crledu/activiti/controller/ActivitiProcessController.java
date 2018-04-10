package com.crledu.activiti.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.Model;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.crledu.activiti.domain.ProcessDefSelector;
import com.crledu.activiti.domain.ProcessDefinitionVo;
import com.crledu.activiti.domain.ProcessInstancessVo;
import com.crledu.activiti.domain.ProcessTaskSelector;
import com.crledu.activiti.domain.ProcessTaskVo;
import com.crledu.activiti.service.processdef.ProcessDefinitionService;
import com.crledu.activiti.service.processins.ProcessInstanceService;
import com.crledu.activiti.service.task.ProcessTaskService;
import com.crledu.system.response.Response;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/activiti")
@SuppressWarnings("unchecked")
public class ActivitiProcessController {

	protected static final Logger LOGGER = LoggerFactory.getLogger(ActivitiProcessController.class);

	@Resource
	private ProcessEngine processEngine;


	@Resource
	private ProcessDefinitionService processDefService;

	@Resource
	private ProcessInstanceService processInsService;
	
	@Resource
	private ProcessTaskService processTaskService;

	/**
	 * 
	 ******************************************
	 * @Function: 通过模型ID，部署流程
	 * @param id
	 *            模型ID
	 * @return Object
	 ******************************************
	 * @CreatedBy: yhy on 2018年4月2日上午10:02:37
	 ******************************************
	 * @ModifiedBy: [name] on [time]
	 * @Description:
	 ******************************************
	 *
	 */
	@RequestMapping(value = "{id}/deployment", method = RequestMethod.GET)
	public Response<String> deploy(@PathVariable("id") String id) {
		try {
			// 获取模型
			RepositoryService repositoryService = processEngine.getRepositoryService();
			Model modelData = repositoryService.getModel(id);
			byte[] bytes = repositoryService.getModelEditorSource(modelData.getId());
			if (bytes == null) {
				return new Response<String>().failure("模型数据为空，请先设计流程并成功保存，再进行部署。");
			}
			JsonNode modelNode = new ObjectMapper().readTree(bytes);
			BpmnModel model = new BpmnJsonConverter()
					.convertToBpmnModel(modelNode); // 获取bpmn模型
			if (model.getProcesses().isEmpty()) {
				return new Response<String>().failure("数据模型不符要求，请至少设计一条主线流程。");
			}
			byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(model);
			// 部署流程
			String processName = modelData.getName() + ".bpmn20.xml";
			Deployment deployment = repositoryService.createDeployment()
					.name(modelData.getName())
					.addString(processName, new String(bpmnBytes, "UTF-8"))
					.deploy();
			modelData.setDeploymentId(deployment.getId());
			repositoryService.saveModel(modelData);
			return new Response<String>().success("流程部署成功");
		} catch (Exception e) {
			LOGGER.error("创建流程失败", e);
			return new Response<String>().failure("流程部署失败", e.getMessage());
		}
	}

	/**
	 * 
	 ******************************************
	 * @Function: 条件获取已部署流程
	 * @param selector
	 * @return Response<List<ProcessDefinitionVo>>
	 ******************************************
	 * @CreatedBy: yhy on 2018年4月2日下午4:48:47
	 ******************************************
	 * @ModifiedBy: [name] on [time]
	 * @Description:
	 ******************************************
	 *
	 */
	@RequestMapping("def/list")
	public Response<List<ProcessDefinitionVo>> findDeploy(
			ProcessDefSelector selector) {
		List<ProcessDefinitionVo> list = processDefService.findDeployBySelector(selector);
		return new Response<List<ProcessDefinitionVo>>().success(list);
	}

	/**
	 * 
	 ******************************************
	 * @Function: 启动流程实例
	 * @param ProDefKey
	 *            流程定义key
	 * @return String
	 ******************************************
	 * @CreatedBy: yhy on 2018年4月2日下午4:53:30
	 ******************************************
	 * @ModifiedBy: [name] on [time]
	 * @Description:
	 ******************************************
	 *
	 */
	@RequestMapping("{key}/start")
	public Response<ProcessInstancessVo> startProcess(@PathVariable("key") String proDefKey, String currentAccount) {
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("userId", currentAccount);
		ProcessInstancessVo instanceVo = processInsService.startInstanceByKey(proDefKey, variables);
		if (instanceVo != null) {
			return new Response<ProcessInstancessVo>().success(instanceVo);
		}
		return new Response<ProcessInstancessVo>().failure("流程启动失败");
	}
	
	@RequestMapping("instance/running")
	public Response<List<ProcessInstancessVo>> findRunningInstance(String[] keyList) {
		List<String> listStr = new ArrayList<>();
		if (keyList != null && keyList.length >0) {
			listStr = Arrays.asList(keyList);
		}
		Set<String> keys = new HashSet<String>(listStr);
		List<ProcessInstancessVo> list = processInsService.findRunningInstance(keys);
		return new Response<List<ProcessInstancessVo>>().success(list);
	}

	/**
	 * 
	 ******************************************
	 * @Function: 通过条件获取，待办任务列表
	 * @param selector
	 * @return Response<List<ProcessTaskVo>> 待办任务
	 ******************************************
	 * @CreatedBy: yhy on 2018年4月4日上午10:08:53
	 ******************************************
	 * @ModifiedBy: [name] on [time]
	 * @Description:
	 ******************************************
	 *
	 */
	@RequestMapping("task/todo/list")
	public Response<List<ProcessTaskVo>> findToDoTasks(ProcessTaskSelector selector) {
		List<ProcessTaskVo> taskVoList = processTaskService.findToDoTasks(selector);
		return new Response<List<ProcessTaskVo>>().success(taskVoList);
	}
	
	/**
	 * 
	 ******************************************
	 * @Function: 根据条件获取已办任务
	 * @param selector
	 * @return
	 * Response<List<ProcessTaskVo>>
	 ******************************************
	 * @CreatedBy: yhy on 2018年4月9日上午9:20:28
	 ******************************************
	 * @ModifiedBy: [name] on [time] 
	 * @Description:
	 ******************************************
	 *
	 */
	@RequestMapping("task/finish/list")
	public Response<List<ProcessTaskVo>> findFinishTasks(ProcessTaskSelector selector) {
		List<ProcessTaskVo> taskVoList = processTaskService.findFinishTasks(selector);
		return new Response<List<ProcessTaskVo>>().success(taskVoList);
	}
	
	/**
	 * 
	 ******************************************
	 * @Function: 任务转交
	 * @param taskId 任务ID
	 * @param userId 用户ID
	 * void
	 ******************************************
	 * @CreatedBy: yhy on 2018年4月9日上午9:06:30
	 ******************************************
	 * @ModifiedBy: [name] on [time] 
	 * @Description:
	 ******************************************
	 *
	 */
	@RequestMapping(value = "task/{id}/turn", method = RequestMethod.POST)
	public Response<String> turnToDoTask(@PathVariable("id") String taskId, String userId) {
		boolean ret = processTaskService.turnToDoTask(taskId, userId);
		if (ret) {
			return new Response<String>().success();
		}
		return new Response<String>().failure("流程出错");
	}
	/**
	 * 
	 ******************************************
	 * @Function: 完成任务
	 * @param taskId 任务ID
	 * @param taskCondition
	 * void
	 ******************************************
	 * @CreatedBy: yhy on 2018年4月9日上午9:21:05
	 ******************************************
	 * @ModifiedBy: [name] on [time] 
	 * @Description:
	 ******************************************
	 *
	 */
	@RequestMapping(value = "task/{id}/complete", method = RequestMethod.POST)
	public Response<String> completeTask(@PathVariable("id") String taskId, ProcessTaskSelector taskCondition) {
		boolean ret = processTaskService.completeTask(taskId, taskCondition);
		if (ret) {
			return new Response<String>().success();
		}
		return new Response<String>().failure("流程出错");
	}

	@RequestMapping(value = "task/{id}/back", method = RequestMethod.GET)
	public void back0Task(@PathVariable("id") String taskId) {
	}

}
