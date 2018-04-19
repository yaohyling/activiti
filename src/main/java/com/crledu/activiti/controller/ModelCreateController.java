package com.crledu.activiti.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngines;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
@RequestMapping("/model")
public class ModelCreateController {
	
	/**
	 * 
	 ******************************************
	 * @Function: 创建模型  由activiti 源码修改
	 * @param modelName 
	 * @param modelKey
	 * @param description
	 * @param request
	 * @param response
	 * void
	 ******************************************
	 * @CreatedBy: yhy on 2018年4月19日上午10:02:11
	 ******************************************
	 * @ModifiedBy: [name] on [time] 
	 * @Description:
	 ******************************************
	 *
	 */
	@RequestMapping("/create")
	public void create(String modelName,String modelKey, String description,HttpServletRequest request, HttpServletResponse response) {
		try {
			ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();

			RepositoryService repositoryService = processEngine.getRepositoryService();

			ObjectMapper objectMapper = new ObjectMapper();
			ObjectNode editorNode = objectMapper.createObjectNode();
			editorNode.put("id", "canvas");
			editorNode.put("resourceId", "canvas");
			ObjectNode stencilSetNode = objectMapper.createObjectNode();
			stencilSetNode.put("namespace", "http://b3mn.org/stencilset/bpmn2.0#");
			editorNode.put("stencilset", stencilSetNode);
			Model modelData = repositoryService.newModel();

			ObjectNode modelObjectNode = objectMapper.createObjectNode();
			modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, modelName);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
			modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
			modelData.setMetaInfo(modelObjectNode.toString());
			modelData.setName(modelName);
			modelData.setKey(modelKey);

			repositoryService.saveModel(modelData);
			repositoryService.addModelEditorSource(modelData.getId(), editorNode.toString().getBytes("utf-8"));
			response.sendRedirect(request.getContextPath() + "/modeler.html?modelId=" + modelData.getId());
		} catch (Exception e) {
			System.out.println("异常");
		}
	}
}
