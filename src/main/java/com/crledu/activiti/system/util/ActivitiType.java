package com.crledu.activiti.system.util;

/**
 * 
 ************************************************************
 * @CopyrightBy: 四川复兴科技有限公司
 ************************************************************
 * @Version: v0.0.1
 * @Function: 储存 activiti 节点类型
 ************************************************************
 * @CreatedBy: yhy on 2018年4月11日下午5:55:09
 ************************************************************
 * @ModifiedBy: [name] on [time]
 * @Description:
 ************************************************************
*
 */
public class ActivitiType {
	// 开始节点
	public static final String START_EVENT = "startEvent";
	// 跳过条件
	public static final String SKIP_EXPRESSION = "_ACTIVITI_SKIP_EXPRESSION_ENABLED";
	// 驳回原因
	public static final String REJECT_REASON = "reject_reason";
}
