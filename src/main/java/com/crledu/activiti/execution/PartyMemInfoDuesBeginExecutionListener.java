package com.crledu.activiti.execution;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class PartyMemInfoDuesBeginExecutionListener implements ExecutionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3678916528669906128L;

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		System.out.println("xxxx");
	}

}
