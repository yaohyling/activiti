package com.crledu.activiti.domain;

import java.io.Serializable;

import com.crledu.system.response.page.PageInfo;

public class ProcessTaskSelector extends PageInfo implements Serializable{
	/**
	 * 当前登录账号
	 */
	private String currentAccount;
	/**
	 * 实例ID
	 */
	private String processInstanceId;
	/**
	 * 委派人
	 */
	private String assignee;

	/**
	 * 角色
	 */
	private String group;
	
	/**
	 * 决定任务流向
	 */
	private boolean result;

	public ProcessTaskSelector() {
		super();
	}

	public ProcessTaskSelector(String processInstanceId, String assignee) {
		super();
		this.processInstanceId = processInstanceId;
		this.assignee = assignee;
	}

	public String getCurrentAccount() {
		return currentAccount;
	}

	public void setCurrentAccount(String currentAccount) {
		this.currentAccount = currentAccount;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
	
	public boolean getResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	@Override	
	public String toString() {
		return "ProcessTaskSelector [currentAccount=" + currentAccount
				+ ", processInstanceId=" + processInstanceId + ", assignee="
				+ assignee + ", group=" + group + "]";
	}

}
