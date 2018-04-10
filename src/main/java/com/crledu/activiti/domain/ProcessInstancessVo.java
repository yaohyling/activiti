package com.crledu.activiti.domain;

import java.io.Serializable;

import org.activiti.engine.runtime.ProcessInstance;

public class ProcessInstancessVo implements Serializable{
		/**
		 * ID
		 */
		private String id;
		/**
		 * 实例名称
		 */
		private String name;
		/**
		 * 流程实例ID
		 */
		private String processInstanceId;
		/**
		 * 节点ID
		 */
		private String activityId;
		/**
		 * 流程定义ID
		 */
		private String processDefinitionId;
		
		/**
		 * 流程定义名称
		 */
		private String processDefinitionName;
		/**
		 * 流程定义key
		 */
		private String processDefinitionKey;
		/**
		 * 流程定义版本
		 */
		private Integer processDefinitionVersion;
		/**
		 * 部署ID
		 */
		private String deploymentId;
		/**
		 * 业务key
		 */
		private String businessKey;
		
		public ProcessInstancessVo() {
			super();
		}
		public ProcessInstancessVo(ProcessInstance instance) {
			super();
			this.id = instance.getId();
			this.name = instance.getName();
			this.processInstanceId = instance.getProcessInstanceId();
			this.activityId = instance.getActivityId();
			this.processDefinitionId = instance.getProcessDefinitionId();
			this.processDefinitionName = instance.getProcessDefinitionName();
			this.processDefinitionKey = instance.getProcessDefinitionKey();
			this.processDefinitionVersion = instance.getProcessDefinitionVersion();
			this.deploymentId = instance.getDeploymentId();
			this.businessKey = instance.getBusinessKey();
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getProcessInstanceId() {
			return processInstanceId;
		}
		public void setProcessInstanceId(String processInstanceId) {
			this.processInstanceId = processInstanceId;
		}
		public String getActivityId() {
			return activityId;
		}
		public void setActivityId(String activityId) {
			this.activityId = activityId;
		}
		public String getProcessDefinitionId() {
			return processDefinitionId;
		}
		public void setProcessDefinitionId(String processDefinitionId) {
			this.processDefinitionId = processDefinitionId;
		}
		public String getProcessDefinitionName() {
			return processDefinitionName;
		}
		public void setProcessDefinitionName(String processDefinitionName) {
			this.processDefinitionName = processDefinitionName;
		}
		public String getProcessDefinitionKey() {
			return processDefinitionKey;
		}
		public void setProcessDefinitionKey(String processDefinitionKey) {
			this.processDefinitionKey = processDefinitionKey;
		}
		public Integer getProcessDefinitionVersion() {
			return processDefinitionVersion;
		}
		public void setProcessDefinitionVersion(Integer processDefinitionVersion) {
			this.processDefinitionVersion = processDefinitionVersion;
		}
		public String getDeploymentId() {
			return deploymentId;
		}
		public void setDeploymentId(String deploymentId) {
			this.deploymentId = deploymentId;
		}
		public String getBusinessKey() {
			return businessKey;
		}
		public void setBusinessKey(String businessKey) {
			this.businessKey = businessKey;
		}
		@Override
		public String toString() {
			return "ProcessInstancessVo [id=" + id + ", name=" + name
					+ ", processInstanceId=" + processInstanceId
					+ ", activityId=" + activityId + ", processDefinitionId="
					+ processDefinitionId + ", processDefinitionName="
					+ processDefinitionName + ", processDefinitionKey="
					+ processDefinitionKey + ", processDefinitionVersion="
					+ processDefinitionVersion + ", deploymentId="
					+ deploymentId + ", businessKey=" + businessKey + "]";
		}
		
}
