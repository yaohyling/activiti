package com.crledu.activiti.service.processdef;

import java.util.List;

import com.crledu.activiti.domain.ProcessDefSelector;
import com.crledu.activiti.domain.ProcessDefinitionVo;

public interface ProcessDefinitionService {
	
	/**
	 * 
	 ******************************************
	 * @Function: 根据条件获取已部署流程定义
	 * @param selector
	 * @return
	 * List<ProcessDefinitionVo>
	 ******************************************
	 * @CreatedBy: yhy on 2018年4月2日下午4:45:16
	 ******************************************
	 * @ModifiedBy: [name] on [time] 
	 * @Description:
	 ******************************************
	 *
	 */
	List<ProcessDefinitionVo> findDeployBySelector(ProcessDefSelector selector);

}
