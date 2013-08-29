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

import java.awt.*;

import javax.swing.*;

import java.io.*;

public class ScpTo {
	public static String[] main(String[] arg) {

		FileInputStream fis = null;
		try {

			String lfile = arg[0];
			String user = arg[1].substring(0, arg[1].indexOf('@'));
			arg[1] = arg[1].substring(arg[1].indexOf('@') + 1);
			String host = arg[1].substring(0, arg[1].indexOf(':'));
			String rfile = arg[1].substring(arg[1].indexOf(':') + 1);

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
				// JOptionPane.showMessageDialog(null,"No SSH keys found.");
				System.out.println("No SSH keys found.");

			}
			// username and password will be given via UserInfo interface.
			UserInfo ui = new MyUserInfo();

			session.setUserInfo(ui);
			session.connect();

			boolean ptimestamp = true;

			System.out.println("rfile " + rfile);
			String command2 = "mkdir -p /" + rfile + " 2>&1; touch " + rfile
					+ "/std{in,out,err}-mpi 2>&1; cd " + rfile;

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

			// exec 'scp -t rfile' remotely
			String command = "scp " + (ptimestamp ? "-p" : "") + " -t" + " -r "
					+ rfile;
			System.out.println("SSH command: " + command);
			Channel channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);

			// get I/O streams for remote scp
			OutputStream out = channel.getOutputStream();
			InputStream in = channel.getInputStream();

			channel.connect();

			if (checkAck(in) != 0) {
				System.exit(0);
			}

			File _lfile = new File(lfile);

			if (ptimestamp) {
				command = "T " + (_lfile.lastModified() / 1000) + " 0";
				// The access time should be sent here,
				// but it is not accessible with JavaAPI ;-<
				command += (" " + (_lfile.lastModified() / 1000) + " 0\n");
				out.write(command.getBytes());
				out.flush();
				if (checkAck(in) != 0) {
					System.exit(0);
				}
			}

			// send "C0644 filesize filename", where filename should not include
			// '/'
			long filesize = _lfile.length();
			command = "C0644 " + filesize + " ";
			if (lfile.lastIndexOf(File.separator) > 0) {
				command += lfile
						.substring(lfile.lastIndexOf(File.separator) + 1);
			} else {
				command += lfile;
			}
			command += "\n";
			out.write(command.getBytes());
			out.flush();
			if (checkAck(in) != 0) {
				System.exit(0);
			}

			// send a content of lfile
			fis = new FileInputStream(lfile);
			byte[] buf = new byte[1024];
			while (true) {
				int len = fis.read(buf, 0, buf.length);
				if (len <= 0)
					break;
				out.write(buf, 0, len); // out.flush();
			}
			fis.close();
			fis = null;
			// send '\0'
			buf[0] = 0;
			out.write(buf, 0, 1);
			out.flush();
			if (checkAck(in) != 0) {
				System.exit(0);
			}
			out.close();

			channel.disconnect();
			// session.disconnect();
			System.out.println("Transfered succeded. Closing SCP channel.");
			System.out.println("rfile " + rfile);
			String command1 = "cd " + rfile + " && /usr/bin/unzip -o " + rfile
					+ "/"
					+ arg[0].substring(arg[0].lastIndexOf(File.separator) + 1)
					+ " ; rm -rf " + rfile + "/"
					+ arg[0].substring(arg[0].lastIndexOf(File.separator) + 1)
					+ " && cd " + rfile + "/" + arg[3]
					+ " && module load nexus && " + Constants.STANDALONE_EXEC
					+ " *.fcs -c `pwd`/" + arg[3] + " -s " + arg[2];
			System.out.println("COMMAND1: " + command1);
			Channel channel1 = session.openChannel("exec");
			((ChannelExec) channel1).setCommand(command1);
			channel1.setInputStream(null);

			((ChannelExec) channel1).setErrStream(System.err);

			InputStream in1 = channel1.getInputStream();

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

			String[] Uauth = new String[] { user, ui.getPassword() };

			return Uauth;
			// System.exit(0);
		} catch (Exception e) {
			System.out.println(e);
			try {
				if (fis != null)
					fis.close();
				return null;
			} catch (Exception ee) {
				return null;
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