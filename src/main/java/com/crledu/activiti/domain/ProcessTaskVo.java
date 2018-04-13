package com.crledu.activiti.domain;

import java.util.Date;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.task.Task;

public class ProcessTaskVo {

	private String taskID;

	private String taskName;

	private String assignee;

	private String owner;

	private String processInstanceId;

	private String category;

	private Date endTime;

	private Date beginTime;
	
	private String description;

	private String formData;

	public ProcessTaskVo() {
		super();
	}

	public ProcessTaskVo(Task task) {
		super();
		this.taskID = task.getId();
		this.taskName = task.getName();
		this.assignee = task.getAssignee();
		this.owner = task.getOwner();
		this.processInstanceId = task.getProcessInstanceId();
		this.category = task.getCategory();
		this.description = task.getDescription();
		this.formData = task.getFormKey();
	}

	public ProcessTaskVo(HistoricTaskInstance task) {
		super();
		this.endTime = task.getEndTime();
		this.taskID = task.getId();
		this.taskName = task.getName();
		this.assignee = task.getAssignee();
		this.owner = task.getOwner();
		this.processInstanceId = task.getProcessInstanceId();
		this.category = task.getCategory();
		this.description = task.getDescription();
		this.formData = task.getFormKey();
	}

	public String getTaskID() {
		return taskID;
	}

	public void setTaskID(String taskID) {
		this.taskID = taskID;
	}

	public String getTaskName() {
		return taskName;
	}

	public void setTaskName(String taskName) {
		this.taskName = taskName;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getFormData() {
		return formData;
	}

	public void setFormData(String formData) {
		this.formData = formData;
	}

	@Override
	public String toString() {
		return "ProcessTaskVo [taskID=" + taskID + ", taskName=" + taskName
				+ ", assignee=" + assignee + ", owner=" + owner
				+ ", processInstanceId=" + processInstanceId + ", category="
				+ category + ", endTime=" + endTime + ", description="
				+ description + ", formData=" + formData + "]";
	}

}
