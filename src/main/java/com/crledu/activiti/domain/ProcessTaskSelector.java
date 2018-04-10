package com.crledu.activiti.domain;

public class ProcessTaskSelector {
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
	 * 每页显示条数
	 */
	private int pageSize;
	
	/**
	 * 页码
	 */
	private int pageNum;

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

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	@Override
	public String toString() {
		return "ProcessTaskSelector [currentAccount=" + currentAccount
				+ ", processInstanceId=" + processInstanceId + ", assignee="
				+ assignee + ", group=" + group + ", pageSize=" + pageSize
				+ ", pageNum=" + pageNum + "]";
	}

}
