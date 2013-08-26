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

import com.bright.cmcall.cmLogin;
import com.bright.cmcall.cmMain;
import com.bright.cmcall.cmLogout;
import com.bright.cmcall.cmReadFile;
import com.bright.cmcall.jobGet;
import com.bright.cmcall.jobSubmit;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.zip.ZipOutputStream;
import java.net.URL;
import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.io.Console;
import java.io.File;
import java.io.FileOutputStream;

import org.apache.commons.io.FileUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpService;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.cookie.Cookie;

import com.bright.utils.ScpTo;
import com.bright.utils.ScpFrom;
import com.bright.utils.ZipFile;
import com.bright.utils.Delete;
import com.bright.json.TextPrompt;

//

public class JSonRequestor {

	public JSonRequestor() {
	}

	private HttpClient getNewHttpClient() {
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore
					.getDefaultType());
			trustStore.load(null, null);
			MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
			sf.setHostnameVerifier(MySSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

			HttpParams params = new BasicHttpParams();
			HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
			HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

			SchemeRegistry registry = new SchemeRegistry();
			registry.register(new Scheme("http", PlainSocketFactory
					.getSocketFactory(), 80));
			registry.register(new Scheme("https", sf, 443));

			ClientConnectionManager ccm = new ThreadSafeClientConnManager(
					params, registry);
			return new DefaultHttpClient(ccm, params);
		} catch (Exception e) {
			return new DefaultHttpClient();
		}
	}

	private String doRequest(String jsonReq, String jsonReq1, String myURL) {
		URL serverURL = null;
		try {

			// HttpClient httpclient = new DefaultHttpClient(); 

			HttpClient httpclient = getNewHttpClient();
			CookieStore cookieStore = new BasicCookieStore();
			HttpContext localContext = new BasicHttpContext();
			localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
			
			/*
			 * httpclient = WebClientDevWrapper.wrapClient(httpclient);
			 */
			
			httpclient.getParams().setParameter(ClientPNames.COOKIE_POLICY,
					CookiePolicy.BROWSER_COMPATIBILITY);
			HttpParams params = httpclient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, 1000);
			HttpConnectionParams.setSoTimeout(params, 1000);
			HttpPost httppost = new HttpPost(myURL);
			StringEntity stringEntity = new StringEntity(jsonReq);
			stringEntity.setContentType("application/json");
			httppost.setEntity(stringEntity);

			System.out
					.println("executing request " + httppost.getRequestLine());
			HttpResponse response = httpclient.execute(httppost, localContext);

			System.out.println(response + "\n");
			for (Cookie c : ((AbstractHttpClient) httpclient).getCookieStore()
					.getCookies()) {
				System.out.println("\n Cookie: " + c.toString() + "\n");
			}

			List<Cookie> cookies = cookieStore.getCookies();
			for (int i = 0; i < cookies.size(); i++) {
				System.out.println("Local cookie: " + cookies.get(i));
			}

			HttpEntity resEntity = response.getEntity();

			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine());
			if (resEntity != null) {
				System.out.println("Response content length: "
						+ resEntity.getContentLength());
				System.out.println("Chunked?: " + resEntity.isChunked());
				System.out.println(EntityUtils.toString(resEntity));
			}
			EntityUtils.consume(resEntity);

			StringEntity stringEntity1 = new StringEntity(jsonReq1);
			stringEntity.setContentType("application/json");
			httppost.setEntity(stringEntity1);
			HttpResponse response1 = httpclient.execute(httppost, localContext);
			System.out.println(response1 + "\n");
			for (Cookie c : ((AbstractHttpClient) httpclient).getCookieStore()
					.getCookies()) {
				System.out.println("\n Cookie: " + c.toString() + "\n");
			}

			List<Cookie> cookies1 = cookieStore.getCookies();
			for (int i = 0; i < cookies1.size(); i++) {
				System.out.println("Local cookie: " + cookies1.get(i));
			}

			HttpEntity resEntity1 = response1.getEntity();

			System.out.println("----------------------------------------");
			System.out.println(response1.getStatusLine());
			if (resEntity1 != null) {
				System.out.println("Response content length: "
						+ resEntity1.getContentLength());
				System.out.println("Chunked?: " + resEntity1.isChunked());
				String message = new String(EntityUtils.toString(resEntity1));
				System.out.println(message);


				return message;
			}

			EntityUtils.consume(resEntity1);

			// Logout and purge cookie on server

			cmLogout logoutReq = new cmLogout();
			logoutReq.setService("logout");

			Gson g = new Gson();
			String json2 = g.toJson(logoutReq);

			StringEntity stringEntity2 = new StringEntity(json2);
			stringEntity.setContentType("application/json");
			httppost.setEntity(stringEntity2);
			HttpResponse response2 = httpclient.execute(httppost, localContext);
			System.out.println(response2 + "\n");

			HttpEntity resEntity2 = response2.getEntity();

			System.out.println("----------------------------------------");
			System.out.println(response2.getStatusLine());
			if (resEntity2 != null) {
				System.out.println("Response content length: "
						+ resEntity2.getContentLength());
				System.out.println("Chunked?: " + resEntity2.isChunked());
				System.out.println(EntityUtils.toString(resEntity2));
			}
			EntityUtils.consume(resEntity2);

			System.out.println("Succesfully Logged Off");

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return null;

	}

	public static void fileSpooler() {

		// New rsync based file transfer
	}

	public static void main(String[] args) throws IOException {

		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new java.io.File("."));
		chooser.setDialogTitle("Select the input directory");

		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);

		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			System.out.println("getCurrentDirectory(): "
					+ chooser.getCurrentDirectory());
			System.out.println("getSelectedFile() : "
					+ chooser.getSelectedFile());

		} else {
			System.out.println("No Selection ");
		}

		// String fileBasename =
		// chooser.getSelectedFile().toString().substring(chooser.getSelectedFile().toString().lastIndexOf(File.separator)+1,chooser.getSelectedFile().toString().lastIndexOf("."));
		String fileBasename = chooser
				.getSelectedFile()
				.toString()
				.substring(
						chooser.getSelectedFile().toString()
								.lastIndexOf(File.separator) + 1);
		System.out.println("Base name: " + fileBasename);

		String[] zipArgs = new String[] {
				chooser.getSelectedFile().toString(),
				chooser.getCurrentDirectory().toString() + File.separator
						+ fileBasename + ".zip" };
		com.bright.utils.ZipFile.main(zipArgs);

		JTextField uiHost = new JTextField(20);
		TextPrompt puiHost = new TextPrompt("hadoop.brightcomputing.com",
				uiHost);
		JTextField uiUser = new JTextField(20);
		TextPrompt puiUser = new TextPrompt("nexus", uiUser);
		JPasswordField uiPass = new JPasswordField(20);
		JTextField uiWdir = new JTextField(20);
		TextPrompt puiWdir = new TextPrompt("/home/nexus/nexus_workdir", uiWdir);
		JTextField uiOut = new JTextField(20);
		TextPrompt puiOut = new TextPrompt("foobar123", uiOut);

		JPanel myPanel = new JPanel(new GridLayout(5, 1));
		myPanel.add(new JLabel("Bright HeadNode hostname:"));
		myPanel.add(uiHost);
		// myPanel.add(Box.createHorizontalStrut(1)); // a spacer
		myPanel.add(new JLabel("Username:"));
		myPanel.add(uiUser);
		myPanel.add(new JLabel("Password:"));
		myPanel.add(uiPass);
		myPanel.add(new JLabel("Working Directory:"));
		myPanel.add(uiWdir);
		// myPanel.add(Box.createHorizontalStrut(1)); // a spacer
		myPanel.add(new JLabel("Output Study Name ( -s ):"));
		myPanel.add(uiOut);

		int result = JOptionPane.showConfirmDialog(null, myPanel,
				"Please fill in all the fields.", JOptionPane.OK_CANCEL_OPTION);
		if (result == JOptionPane.OK_OPTION) {
			System.out.println("Input received.");

		}

		String rfile = uiWdir.getText();
		String rhost = uiHost.getText();
		String ruser = uiUser.getText();
		String nexusOut = uiOut.getText();
		String rpass = uiPass.getText();

		String[] myarg = new String[] { zipArgs[1],
				ruser + "@" + rhost + ":" + rfile, nexusOut, fileBasename };
		String[] Uauth = com.bright.utils.ScpTo.main(myarg);

		cmLogin loginReq = new cmLogin();
		loginReq.setService("login");
		try {
			loginReq.setUsername(ruser);
			loginReq.setPassword(rpass);

		} catch (NullPointerException e) {
			System.out.println("cancelled due to incorrect input");
			System.exit(0);

		}
		String cmURL = "https://" + rhost + ":8081/json";
		cmMain mainreq = new cmMain();
		mainreq.setService("cmmain");
		mainreq.setCall("getMasterIPs");

		jobSubmit myjob = new jobSubmit();
		jobSubmit.jobObject myjobObj = new jobSubmit.jobObject();

		myjob.setService("cmjob");
		myjob.setCall("submitJob");

		myjobObj.setQueue("defq");
		myjobObj.setJobname("myNexusJob");
		myjobObj.setAccount(ruser);
		myjobObj.setRundirectory(rfile);
		myjobObj.setUsername(ruser);
		myjobObj.setGroupname("cmsupport");
		myjobObj.setPriority("1");
		myjobObj.setStdinfile(rfile + "/stdin-mpi");
		myjobObj.setStdoutfile(rfile + "/stdout-mpi");
		myjobObj.setStderrfile(rfile + "/stderr-mpi");
		myjobObj.setResourceList(Arrays.asList(""));
		myjobObj.setDependencies(Arrays.asList(""));
		myjobObj.setMailNotify(false);
		myjobObj.setMailOptions("ALL");
		myjobObj.setMaxWallClock("00:10:00");
		myjobObj.setNumberOfProcesses(1);
		myjobObj.setNumberOfNodes(1);
		myjobObj.setNodes(Arrays.asList(""));
		myjobObj.setCommandLineInterpreter("/bin/bash");
		myjobObj.setUserdefined(Arrays.asList("cd " + rfile, "date", "pwd"));
		myjobObj.setExecutable("mpirun");
		myjobObj.setArguments("-env I_MPI_FABRICS shm:tcp /cm/shared/apps/nexus/nexussimulators/LinuxEM64/nexusEM64_5000_4_7.exe -mpi -c "
				+ rfile
				+ "/"
				+ fileBasename
				+ "/"
				+ fileBasename
				+ " -s "
				+ rfile + "/" + fileBasename + "/" + nexusOut);
		myjobObj.setModules(Arrays.asList("shared", "nexus","intel-mpi/64"));
		myjobObj.setDebug(false);
		myjobObj.setBaseType("Job");
		myjobObj.setIsSlurm(true);
		myjobObj.setUniqueKey(0);
		myjobObj.setModified(false);
		myjobObj.setToBeRemoved(false);
		myjobObj.setChildType("SlurmJob");
		myjobObj.setJobID("Nexus test");

		// Map<String,jobSubmit.jobObject > mymap= new HashMap<String,
		// jobSubmit.jobObject>();
		// mymap.put("Slurm",myjobObj);
		ArrayList mylist = new ArrayList();
		mylist.add("slurm");
		mylist.add(myjobObj);
		myjob.setArgs(mylist);

		GsonBuilder builder = new GsonBuilder();
		builder.enableComplexMapKeySerialization();

		// Gson g = new Gson();
		Gson g = builder.create();
		String json = g.toJson(loginReq);
		System.out.println("JSON Request No. 1 " + json);
		String json1 = g.toJson(mainreq);
		System.out.println("JSON Request No. 2 " + json1);
		String json2 = g.toJson(myjob);
		System.out.println("JSON Request No. 3 " + json2);

		// To be used from a real console and not Eclipse
		Delete.main(zipArgs[1]);
		// JSonRequestor AuthRequestor = new JSonRequestor();
		// AuthRequestor.AuthRequest(cmURL);
		JSonRequestor jSonRequestor = new JSonRequestor();
		// jSonRequestor.doRequest(json, cmURL );

		String message = jSonRequestor.doRequest(json, json2, cmURL);
		Scanner resInt = new Scanner(message).useDelimiter("[^0-9]+");
		int jobID = resInt.nextInt();
		System.out.println("Job ID: " + jobID);

		JOptionPane optionPane = new JOptionPane(message);
		JDialog myDialog = optionPane.createDialog(null, "CMDaemon response: ");
		myDialog.setModal(false);
		myDialog.setVisible(true);

		ArrayList mylist2 = new ArrayList();
		mylist2.add("slurm");
		String JobID = Integer.toString(jobID);
		mylist2.add(JobID);
		myjob.setArgs(mylist2);
		myjob.setService("cmjob");
		myjob.setCall("getJob");
		String json3 = g.toJson(myjob);
		System.out.println("JSON Request No. 4 " + json3);

		cmReadFile readfile = new cmReadFile();
		readfile.setService("cmmain");
		readfile.setCall("readFile");
		readfile.setUserName(ruser);
		readfile.setPath(rfile + "/" + fileBasename + "/" + fileBasename
				+ ".sum");
		String json4 = g.toJson(readfile);

		String getJobJSON = jSonRequestor.doRequest(json, json3, cmURL);
		jobGet getJobObj = new Gson().fromJson(getJobJSON, jobGet.class);
		System.out.println("Job " + jobID + " status: "
				+ getJobObj.getStatus().toString());

		while (getJobObj.getStatus().toString().equals("RUNNING")
				|| getJobObj.getStatus().toString().equals("COMPLETING")) {
			try {

				getJobJSON = jSonRequestor.doRequest(json, json3, cmURL);
				getJobObj = new Gson().fromJson(getJobJSON, jobGet.class);
				System.out.println("Job " + jobID + " status: "
						+ getJobObj.getStatus().toString());
				Thread.sleep(10000);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}

		}

		Gson gson_nice = new GsonBuilder().setPrettyPrinting().create();
		String json_out = gson_nice.toJson(getJobJSON);
		System.out.println(json_out);
		System.out.println("JSON Request No. 5 " + json4);
		String monFile = jSonRequestor.doRequest(json, json4, cmURL);
		System.out.print("Monitoring file: "
				+ monFile.replaceAll("\n+",
						System.getProperty("line.separator")));
		FileUtils
				.writeStringToFile(new File(chooser.getCurrentDirectory()
						.toString()
						+ File.separator
						+ fileBasename
						+ ".sum.txt"), monFile.replaceAll("\n+",
						System.getProperty("line.separator")));

		if (getJobObj.getStatus().toString().equals("COMPLETED")) {
			String[] zipArgs_from = new String[] {
					chooser.getSelectedFile().toString(),
					chooser.getCurrentDirectory().toString() + File.separator
							+ fileBasename + "_out.zip" };
			String[] myarg_from = new String[] {
					ruser + "@" + rhost + ":" + rfile + "/" + fileBasename
							+ "_out.zip", zipArgs_from[1], rfile, fileBasename };
			com.bright.utils.ScpFrom.main(myarg_from);
			monFile = jSonRequestor.doRequest(json, json4, cmURL);
			System.out.print(monFile.replaceAll("\n",
					System.getProperty("line.separator")));
			JOptionPane optionPaneS = new JOptionPane(
					"Job execution completed without errors!");
			JDialog myDialogS = optionPaneS.createDialog(null, "Job status: ");
			myDialogS.setModal(false);
			myDialogS.setVisible(true);

		} else {
			JOptionPane optionPaneF = new JOptionPane("Job execution FAILED!");
			JDialog myDialogF = optionPaneF.createDialog(null, "Job status: ");
			myDialogF.setModal(false);
			myDialogF.setVisible(true);
		}

	}

}
