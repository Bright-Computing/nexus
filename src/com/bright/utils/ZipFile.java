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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.apache.commons.io.IOUtils;

public class ZipFile {

	public static void main(String[] args) {

		try {
			// name of zip file to create
			String outFilename = args[1];

			// create ZipOutputStream object
			ZipOutputStream out = new ZipOutputStream(new FileOutputStream(
					outFilename));

			// path to the folder to be zipped
			File zipFolder = new File(args[0]);

			int len = zipFolder.getAbsolutePath().lastIndexOf(File.separator);
			String baseName = zipFolder.getAbsolutePath().substring(0, len + 1);

			addFolderToZip(zipFolder, out, baseName);
			System.out.println("Created ZIP file: " + args[1]);

			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void addFolderToZip(File folder, ZipOutputStream zip,
			String baseName) throws IOException {
		File[] files = folder.listFiles();
		for (File file : files) {
			if (file.isDirectory()) {
				addFolderToZip(file, zip, baseName);
			} else {
				String name = file.getAbsolutePath().substring(
						baseName.length());
				ZipEntry zipEntry = new ZipEntry(name);
				zip.putNextEntry(zipEntry);
				IOUtils.copy(new FileInputStream(file), zip);
				zip.closeEntry();
			}
		}
	}
}