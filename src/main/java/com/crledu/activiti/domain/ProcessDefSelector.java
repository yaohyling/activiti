package com.crledu.activiti.domain;

/**
 * 
 ************************************************************
 * @CopyrightBy: 四川复兴科技有限公司
 ************************************************************
 * @Version: v0.0.1
 * @Function: 已部署流程定义selector
 ************************************************************
 * @CreatedBy: yhy on 2018年4月2日上午10:24:53
 ************************************************************
 * @ModifiedBy: [name] on [time]
 * @Description:
 ************************************************************
 *
 */
public class ProcessDefSelector {
	/**
	 * 流程定义ID
	 */
	private String ProcessDefId;
	
	/**
	 * 流程定义key
	 */
	private String ProcessDefKey;
	
	/**
	 * 流程定义名称
	 */
	private String ProcessDefName;
	
	/**
	 * 版本
	 */
	private String version;
	
	public ProcessDefSelector() {
		super();
	}

	public ProcessDefSelector(String processDefId, String processDefKey,
			String processDefName, String version) {
		super();
		ProcessDefId = processDefId;
		ProcessDefKey = processDefKey;
		ProcessDefName = processDefName;
		this.version = version;
	}

	public String getProcessDefId() {
		return ProcessDefId;
	}

	public void setProcessDefId(String processDefId) {
		ProcessDefId = processDefId;
	}

	public String getProcessDefKey() {
		return ProcessDefKey;
	}

	public void setProcessDefKey(String processDefKey) {
		ProcessDefKey = processDefKey;
	}

	public String getProcessDefName() {
		return ProcessDefName;
	}

	public void setProcessDefName(String processDefName) {
		ProcessDefName = processDefName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
