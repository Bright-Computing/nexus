/*
 * Copyright (c) 2004-2013 Bright Computing, Inc. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of
 * Bright Computing, Inc. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with Bright Computing, Inc.
 */

package com.bright.utils;

import com.bright.json.Constants;
import com.jcraft.jsch.*;

import java.io.*;

public class ScpFrom {
	public static void main(String[] arg) {

		FileOutputStream fos = null;
		try {

			String user = arg[0].substring(0, arg[0].indexOf('@'));
			arg[0] = arg[0].substring(arg[0].indexOf('@') + 1);
			String host = arg[0].substring(0, arg[0].indexOf(':'));
			String rfile = arg[0].substring(arg[0].indexOf(':') + 1);
			String lfile = arg[1];

			String prefix = null;
			if (new File(lfile).isDirectory()) {
				prefix = lfile + File.separator;
			}

			JSch jsch = new JSch();
			Session session = jsch.getSession(user, host, 22);

			try {
				jsch.setKnownHosts(System.getProperty("user.home")
						+ File.separator + Constants.BRIGHT_CACHE_DIR
						+ File.separator + "known_hosts");
				jsch.addIdentity(System.getProperty("user.home")
						+ File.separator + Constants.BRIGHT_CACHE_DIR
						+ File.separator + "id_dsa");
			} catch (Exception e) {
				System.out.println("No SSH keys found.");

			}

			// username and password will be given via UserInfo interface.
			UserInfo ui = new MyUserInfo();
			session.setUserInfo(ui);
			session.connect();

			String myDir = arg[2] + "/" + arg[3];
			String command2 = "cd " + arg[2] + " && /usr/bin/zip -r " + arg[3]
					+ "_out.zip " + arg[3] + " -x " + myDir + "/"
					+ "{nexus_data, *.fcs, .* , *.rst, *.rpt}"
					+ " 2>&1 > /dev/null";
			System.out.println("cmd2: " + command2);

			Channel channel2 = session.openChannel("exec");
			((ChannelExec) channel2).setCommand(command2);
			channel2.setInputStream(null);

			((ChannelExec) channel2).setErrStream(System.err);

			InputStream in2 = channel2.getInputStream();

			channel2.connect();

			byte[] tmp1 = new byte[1024];
			while (true) {
				while (in2.available() > 0) {
					int i = in2.read(tmp1, 0, 1024);
					if (i < 0)
						break;
					System.out.print(new String(tmp1, 0, i));
				}
				if (channel2.isClosed()) {
					System.out.println("exit-status: "
							+ channel2.getExitStatus());
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
			}
			channel2.disconnect();

			// exec 'scp -f rfile' remotely
			String command = "scp -f " + rfile;
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);

			// get I/O streams for remote scp
			OutputStream out = channel.getOutputStream();
			InputStream in = channel.getInputStream();

			channel.connect();

			byte[] buf = new byte[1024];

			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();

			while (true) {
				int c = checkAck(in);
				if (c != 'C') {
					break;
				}

				// read '0644 '
				in.read(buf, 0, 5);

				long filesize = 0L;
				while (true) {
					if (in.read(buf, 0, 1) < 0) {
						// error
						break;
					}
					if (buf[0] == ' ')
						break;
					filesize = filesize * 10L + (long) (buf[0] - '0');
				}

				String file = null;
				for (int i = 0;; i++) {
					in.read(buf, i, 1);
					if (buf[i] == (byte) 0x0a) {
						file = new String(buf, 0, i);
						break;
					}
				}

				// System.out.println("filesize="+filesize+", file="+file);

				// send '\0'
				buf[0] = 0;
				out.write(buf, 0, 1);
				out.flush();

				// read a content of lfile
				fos = new FileOutputStream(prefix == null ? lfile : prefix
						+ file);
				int foo;
				while (true) {
					if (buf.length < filesize)
						foo = buf.length;
					else
						foo = (int) filesize;
					foo = in.read(buf, 0, foo);
					if (foo < 0) {
						// error
						break;
					}
					fos.write(buf, 0, foo);
					filesize -= foo;
					if (filesize == 0L)
						break;
				}
				fos.close();
				fos = null;

				if (checkAck(in) != 0) {
					System.exit(0);
				}

				// send '\0'
				buf[0] = 0;
				out.write(buf, 0, 1);
				out.flush();
			}

			String command1 = "rm -rf " + rfile;
			System.out.println("COMMAND1: " + command1);
			Channel channel1 = session.openChannel("exec");
			((ChannelExec) channel1).setCommand(command1);
			channel1.setInputStream(null);

			((ChannelExec) channel1).setErrStream(System.err);

			//InputStream in1 = channel1.getInputStream();

			channel1.connect();

			byte[] tmp = new byte[1024];
			while (true) {
				while (in.available() > 0) {
					int i = in.read(tmp, 0, 1024);
					if (i < 0)
						break;
					System.out.print(new String(tmp, 0, i));
				}
				if (channel1.isClosed()) {
					System.out.println("exit-status: "
							+ channel1.getExitStatus());
					break;
				}
				try {
					Thread.sleep(1000);
				} catch (Exception ee) {
				}
			}
			channel1.disconnect();

			session.disconnect();
			System.out
					.print("Transferred file " + arg[1] + " to local system.");

			
		} catch (Exception e) {
			System.out.println(e);
			try {
				if (fos != null)
					fos.close();
			} catch (Exception ee) {
			}
		}
	}

	static int checkAck(InputStream in) throws IOException {
		int b = in.read();
		// b may be 0 for success,
		// 1 for error,
		// 2 for fatal error,
		// -1
		if (b == 0)
			return b;
		if (b == -1)
			return b;

		if (b == 1 || b == 2) {
			StringBuffer sb = new StringBuffer();
			int c;
			do {
				c = in.read();
				sb.append((char) c);
			} while (c != '\n');
			if (b == 1) { // error
				System.out.print(sb.toString());
			}
			if (b == 2) { // fatal error
				System.out.print(sb.toString());
			}
		}
		return b;
	}
}