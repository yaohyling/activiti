package com.crledu.system.response;

import java.io.Serializable;

/**
 * 
 ************************************************************
 * @CopyrightBy: 四川复兴科技有限公司
 ************************************************************
 * @Version: v0.0.1
 * @Function: Response响应封装
 ************************************************************
 * @CreatedBy: yhy on 2018年3月20日下午2:34:16
 ************************************************************
 * @ModifiedBy: [name] on [time]
 * @Description:
 ************************************************************
*
 */
@SuppressWarnings("rawtypes")
public class Response<T> implements Serializable {
	private Meta meta;
	private T data;

	public Response success() {
		this.meta = new Meta(ResponseState.SUCCESS);
		return this;
	}

	public Response success(T data) {
		success();
		this.data = data;
		return this;
	}

	public Response message(String message) {
		getMeta().setMessage(message);
		return this;
	}

	public Response data(T data) {
		this.data = data;
		return this;
	}

	public Response failure() {
		this.meta = new Meta(ResponseState.FAIL);
		return this;
	}

	public Response failure(String message) {
		this.meta = new Meta(ResponseState.FAIL, message);
		return this;
	}

	public Response failure( String message, T data) {
		failure(message);
		this.data = data;
		return this;
	}

	public Meta getMeta() {
		return this.meta;
	}

	public T getData() {
		return this.data;
	}

	public Response setData(T data) {
		this.data = data;
		return this;
	}

	public static class Meta implements Serializable {
		private int code;
		private String message;

		public Meta() {
		}

		public Meta(ResponseState responseState) {
			setCode(responseState.getCode());
			this.message = responseState.getCodeInfo();
		}

		public Meta(ResponseState responseState, String message) {
			if (message == null) {
				message = responseState.getCodeInfo();
			}
			this.message = message;
		}

		public int getCode() {
			return this.code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public void setCode(ResponseState state) {
			this.code = state.getCode();
		}

		public String getMessage() {
			return this.message;
		}

		public void setMessage(String message) {
			this.message = message;
		}
	}
}
