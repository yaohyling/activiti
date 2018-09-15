package com.crledu.activiti.system.util;

import java.io.Serializable;

import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.persistence.entity.ExecutionEntityManager;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;

/**
 * 任务驳回方法支持
 *
 * @author create by 叶云轩 at 2018/1/15 09:32
 */
public class RejectTaskCMD implements Command<Object>, Serializable {

    /**
     * 当前任务
     */
    private HistoricTaskInstance currentTaskInstance;
    /**
     * 目标任务
     */
    private HistoricTaskInstance destinationTaskInstance;
    /**
     * 目标任务节点
     */
    private ActivityImpl destinationActivity;

    
    public RejectTaskCMD(HistoricTaskInstance currentTaskInstance
            , HistoricTaskInstance destinationTaskInstance
            , ActivityImpl destinationActivity) {
        this.currentTaskInstance = currentTaskInstance;
        this.destinationTaskInstance = destinationTaskInstance;
        this.destinationActivity = destinationActivity;
    }

    @Override
    public Object execute(CommandContext commandContext) {
        // 流程实例ID
        String processInstanceId = destinationTaskInstance.getProcessInstanceId();
        // 执行管理器
        ExecutionEntityManager executionEntityManager = commandContext.getExecutionEntityManager();
        // 通过执行ID，获取执行实体
        ExecutionEntity executionEntity = executionEntityManager.findExecutionById(processInstanceId);
        // 当前活跃的节点信息
        ActivityImpl currentActivity = executionEntity.getActivity();
        // 创建一个出口转向
        TransitionImpl outgoingTransition = currentActivity.createOutgoingTransition();
        // 封装目标节点到转向实体
        outgoingTransition.setDestination(destinationActivity);
        // 流程转向
        executionEntity.setTransition(outgoingTransition);
        return null;
    }
}