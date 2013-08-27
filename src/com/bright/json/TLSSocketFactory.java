/*
 * Copyright (c) 2004-2013 Bright Computing, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Bright Computing, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Bright Computing, Inc.
 */

package com.bright.json;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

public abstract class TLSSocketFactory extends SSLSocketFactory {

	private final javax.net.ssl.SSLSocketFactory socketfactory;

	public TLSSocketFactory(SSLContext sslContext) {
		super();

		this.socketfactory = sslContext.getSocketFactory();
	}

	public Socket createSocket() throws IOException {
		SSLSocket socket = (SSLSocket) super.createSocket();

		socket.setEnabledProtocols(new String[] { "SSLv3, TLSv1" });

		return socket;
	}

	public Socket createSocket(final Socket socket, final String host,
			final int port, final boolean autoClose) throws IOException,
			UnknownHostException {

		SSLSocket sslSocket = (SSLSocket) this.socketfactory.createSocket(
				socket, host, port, autoClose);

		sslSocket.setEnabledProtocols(new String[] { "SSLv3", "TLSv1" });

		// getHostnameVerifier().verify(host, sslSocket);

		return sslSocket;
	}
}