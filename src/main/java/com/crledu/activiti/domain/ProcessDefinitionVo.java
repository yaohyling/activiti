package com.crledu.activiti.domain;

import org.activiti.engine.repository.ProcessDefinition;

public class ProcessDefinitionVo {
	private String processDefName;

	private String processDefKey;

	private Integer processDefVersion;

	public ProcessDefinitionVo() {
		super();
	}

	public ProcessDefinitionVo(ProcessDefinition processDefinition) {
		this.processDefName = processDefinition.getName();
		this.processDefKey = processDefinition.getKey();
		this.processDefVersion = processDefinition.getVersion();
	}

	public String getProcessDefName() {
		return processDefName;
	}

	public void setProcessDefName(String processDefName) {
		this.processDefName = processDefName;
	}

	public String getProcessDefKey() {
		return processDefKey;
	}

	public void setProcessDefKey(String processDefKey) {
		this.processDefKey = processDefKey;
	}

	public Integer getProcessDefVersion() {
		return processDefVersion;
	}

	public void setProcessDefVersion(Integer processDefVersion) {
		this.processDefVersion = processDefVersion;
	}
}
