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

import java.util.List;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.apache.commons.io.FileUtils;

public class rmDuplicateLines {

	public static void main(String args) {
		File monfile = new File(args);

		Set<String> userIdSet = new LinkedHashSet<String>();

		if (monfile.isFile() && monfile.getName().endsWith(".txt")) {
			try {
				List<String> content = FileUtils.readLines(monfile,
						Charset.forName("UTF-8"));
				userIdSet.addAll(content);
				
		        Iterator<String> itr =userIdSet.iterator();
		        StringBuffer output = new StringBuffer();
		        while(itr.hasNext()){
		        	output.append(itr.next() +  System.getProperty("line.separator"));		            
		        }
				
				BufferedWriter out = new BufferedWriter(new FileWriter(monfile));
				String outText = output.toString();

				out.write(outText);

				out.close();
			} catch (IOException e) {
				e.printStackTrace();

			}
		}

	}
}
