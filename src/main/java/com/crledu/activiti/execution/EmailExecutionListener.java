package com.crledu.activiti.execution;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

public class EmailExecutionListener implements ExecutionListener {


	private static final long serialVersionUID = 4687831170776335686L;


	@Override
	public void notify(DelegateExecution execution) throws Exception {
		System.out.println("asdasdwqd");
	}

	
	
}
