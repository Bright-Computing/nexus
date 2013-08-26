/*
 * Copyright (c) 2004-2013 Bright Computing, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Bright Computing, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Bright Computing, Inc.
 */

package com.bright.cmcall;

import java.util.List;

public class cmMain {
	private String service;
	private String call;
	private List args;

	// other setter and getter

	public void setService(String service) {
		this.service = service;
	}

	public void setArgs(List args) {
		this.args = args;
	}

	public void setCall(String call) {
		this.call = call;
	}

}