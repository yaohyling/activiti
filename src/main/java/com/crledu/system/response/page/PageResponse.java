package com.crledu.system.response.page;

import java.io.Serializable;
import java.util.List;

/**
 * 
 ************************************************************
 * @CopyrightBy: 四川复兴科技有限公司
 ************************************************************
 * @Version: v0.0.1
 * @Function: 分页响应数据格式封装
 ************************************************************
 * @CreatedBy: yhy on 2018年3月20日下午2:36:06
 ************************************************************
 * @ModifiedBy: [name] on [time]
 * @Description:
 ************************************************************
*
 */
public class PageResponse<E> implements Serializable {
	
	private Long total;
	
	private List<E> rows;

	public PageResponse() {
		super();
	}

	public PageResponse(List<E> rows, Long total) {
		super();
		this.rows = rows;
		this.total = total;
	}

	public List<E> getRows() {
		return rows;
	}

	public void setRows(List<E> rows) {
		this.rows = rows;
	}

	public Long getTotal() {
		return total;
	}

	public void setTotal(Long total) {
		this.total = total;
	}

	@Override
	public String toString() {
		return "PageResponse [rows=" + rows + ", total=" + total + "]";
	}

}
