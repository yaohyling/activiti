package com.crledu.system.response;

import java.io.Serializable;

/**
 * 
 ************************************************************
 * @CopyrightBy: 四川复兴科技有限公司
 ************************************************************
 * @Version: v0.0.1
 * @Function: Response响应状态枚举
 ************************************************************
 * @CreatedBy: yhy on 2018年3月20日下午2:35:19
 ************************************************************
 * @ModifiedBy: [name] on [time]
 * @Description:
 ************************************************************
*
 */
public enum ResponseState implements Serializable {

	FAIL(0, "失败"), SUCCESS(1, "成功");

	private int code;
	private String codeInfo;

	private ResponseState(int code, String codeInfo) {
		this.code = code;
		this.codeInfo = codeInfo;
	}

	public int getCode() {
		return this.code;
	}

	public String getCodeInfo() {
		return this.codeInfo;
	}

}
