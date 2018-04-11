package com.crledu.activiti.system.util;

import java.io.Serializable;
import java.util.List;

import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntity;
import org.activiti.engine.impl.persistence.entity.HistoricActivityInstanceEntityManager;

public class RejectActivityCMD implements Command<List<HistoricActivityInstanceEntity>>, Serializable {
	
	/**
	 * 实例ID
	 */
	private String instanceId;
	
	/**
	 * 需要恢复的历史节点
	 */
	private List<HistoricActivityInstanceEntity> oldHisActivityList;
	

	public RejectActivityCMD(String instanceId,
			List<HistoricActivityInstanceEntity> oldHisActivityList) {
		super();
		this.instanceId = instanceId;
		this.oldHisActivityList = oldHisActivityList;
	}



	@Override
	public List<HistoricActivityInstanceEntity> execute(CommandContext commandContext) {
		// 获取历史活动节点实例管理器
		HistoricActivityInstanceEntityManager historicActivityInstanceEntityManager = commandContext.getHistoricActivityInstanceEntityManager();
		// 根据流程实例编号删除所有的历史活动节点
		historicActivityInstanceEntityManager.deleteHistoricActivityInstancesByProcessInstanceId(instanceId);
		// 提交到数据库
		commandContext.getDbSqlSession().flush();
		// 恢复历史节点
		for (HistoricActivityInstanceEntity historicActivityInstance : oldHisActivityList) {
			historicActivityInstanceEntityManager.insertHistoricActivityInstance(historicActivityInstance);
		}
		// 提交到数据库
		commandContext.getDbSqlSession().flush();
		return null;
	}

}
