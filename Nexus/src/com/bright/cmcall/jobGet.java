package com.bright.cmcall;

import java.util.List;

public class jobGet {

	private List<String> modules;
	private String jobID;
	private String queue;
	private String jobname;
	private String account;
	private String rundirectory;
	private String username;
	private String groupname;
	private String priority;
	private String stdinfile;
	private String stdoutfile;
	private String stderrfile;
	private List<String> resourceList;
	private List<String> dependencies;
	private Boolean mailNotify;
	private String mailOptions;
	private String mailList;
	private String maxWallClock;
	private int numberOfProcesses;
	private int numberOfNodes;
	private List<String> nodes;
	private String commandLineInterpreter;
	private List<String> userdefined;
	private String executable;
	private String arguments;
	private Boolean debug;
	private Boolean isSlurmJob;
	private String childType;
	private String baseType;
	private int uniqueKey;
	private int oldLocalUniqueKey;
	private String revision;
	private Boolean modified;
	private Boolean toBeRemoved;
	private String status;
	private String project;
	private String inqueue;
	private String submittime;
	private String starttime;
	private String scriptFile;
	private String userGroup;
	private String parallelEnvironment;
	private List<String> environmentVariables;
	private String type;
	private Boolean isSGEJob;
	private Boolean isCondorJob;
	private Boolean isLSFBaseJob;
	private Boolean isLSFJob;
	private Boolean isOpenLavaJob;
	private Boolean isPBSJob;
	private Boolean isTorqueJob;
	private Boolean isPbsProJob;

	public List<String> getModules() {
		return modules;
	}

	public String getJobID() {
		return jobID;
	}

	public String getQueue() {
		return queue;
	}

	public String getJobname() {
		return jobname;
	}

	public String getAccount() {
		return account;
	}

	public String getRundirectory() {
		return rundirectory;
	}

	public String getUsername() {
		return username;
	}

	public String getGroupname() {
		return groupname;
	}

	public String getPriority() {
		return priority;
	}

	public String getStdinfile() {
		return stdinfile;
	}

	public String getStdoutfile() {
		return stdoutfile;
	}

	public String getStderrfile() {
		return stderrfile;
	}

	public List<String> getResourceList() {
		return resourceList;
	}

	public List<String> getDependencies() {
		return dependencies;
	}

	public Boolean getMailNotify() {
		return mailNotify;
	}

	public String getMailOptions() {
		return mailOptions;
	}

	public String getMailList() {
		return mailList;
	}

	public String getMaxWallClock() {
		return maxWallClock;
	}

	public int getNumberOfProcesses() {
		return numberOfProcesses;
	}

	public int getNumberOfNodes() {
		return numberOfNodes;
	}

	public List<String> getNodes() {
		return nodes;
	}

	public String getCommandLineInterpreter() {
		return commandLineInterpreter;
	}

	public List<String> getUserdefined() {
		return userdefined;
	}

	public String getExecutable() {
		return executable;
	}

	public String getArguments() {
		return arguments;
	}

	public Boolean getDebug() {
		return debug;
	}

	public Boolean getIsSlurmJob() {
		return isSlurmJob;
	}

	public String getChildType() {
		return childType;
	}

	public String getBaseType() {
		return baseType;
	}

	public int getUniqueKey() {
		return uniqueKey;
	}

	public int getOldLocalUniqueKey() {
		return oldLocalUniqueKey;
	}

	public String getRevision() {
		return revision;
	}

	public Boolean getModified() {
		return modified;
	}

	public Boolean getToBeRemoved() {
		return toBeRemoved;
	}

	public String getStatus() {
		return status;
	}

	public String getProject() {
		return project;
	}

	public String getInqueue() {
		return inqueue;
	}

	public String getSubmittime() {
		return submittime;
	}

	public String getStarttime() {
		return starttime;
	}

	public String getScriptFile() {
		return scriptFile;
	}

	public String getUserGroup() {
		return userGroup;
	}

	public String getParallelEnvironment() {
		return parallelEnvironment;
	}

	public List<String> getEnvironmentVariables() {
		return environmentVariables;
	}

	public String getType() {
		return type;
	}

	public Boolean getIsSGEJob() {
		return isSGEJob;
	}

	public Boolean getIsCondorJob() {
		return isCondorJob;
	}

	public Boolean getIsLSFBaseJob() {
		return isLSFBaseJob;
	}

	public Boolean getIsLSFJob() {
		return isLSFJob;
	}

	public Boolean getIsOpenLavaJob() {
		return isOpenLavaJob;
	}

	public Boolean getIsPBSJob() {
		return isPBSJob;
	}

	public Boolean getIsTorqueJob() {
		return isTorqueJob;
	}

	public Boolean getIsPbsProJob() {
		return isPbsProJob;
	}

	public void setModules(List<String> modules) {
		this.modules = modules;
	}

	public void setJobID(String jobID) {
		this.jobID = jobID;
	}

	public void setQueue(String queue) {
		this.queue = queue;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public void setRundirectory(String rundirectory) {
		this.rundirectory = rundirectory;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setGroupname(String groupname) {
		this.groupname = groupname;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public void setStdinfile(String stdinfile) {
		this.stdinfile = stdinfile;
	}

	public void setStdoutfile(String stdoutfile) {
		this.stdoutfile = stdoutfile;
	}

	public void setStderrfile(String stderrfile) {
		this.stderrfile = stderrfile;
	}

	public void setResourceList(List<String> resourceList) {
		this.resourceList = resourceList;
	}

	public void setDependencies(List<String> dependencies) {
		this.dependencies = dependencies;
	}

	public void setMailNotify(Boolean mailNotify) {
		this.mailNotify = mailNotify;
	}

	public void setMailOptions(String mailOptions) {
		this.mailOptions = mailOptions;
	}

	public void setMailList(String mailList) {
		this.mailList = mailList;
	}

	public void setMaxWallClock(String maxWallClock) {
		this.maxWallClock = maxWallClock;
	}

	public void setNumberOfProcesses(int numberOfProcesses) {
		this.numberOfProcesses = numberOfProcesses;
	}

	public void setNumberOfNodes(int numberOfNodes) {
		this.numberOfNodes = numberOfNodes;
	}

	public void setNodes(List<String> nodes) {
		this.nodes = nodes;
	}

	public void setCommandLineInterpreter(String commandLineInterpreter) {
		this.commandLineInterpreter = commandLineInterpreter;
	}

	public void setUserdefined(List<String> userdefined) {
		this.userdefined = userdefined;
	}

	public void setExecutable(String executable) {
		this.executable = executable;
	}

	public void setArguments(String arguments) {
		this.arguments = arguments;
	}

	public void setDebug(Boolean debug) {
		this.debug = debug;
	}

	public void setIsSlurmJob(Boolean isSlurmJob) {
		this.isSlurmJob = isSlurmJob;
	}

	public void setChildType(String childType) {
		this.childType = childType;
	}

	public void setBaseType(String baseType) {
		this.baseType = baseType;
	}

	public void setUniqueKey(int uniqueKey) {
		this.uniqueKey = uniqueKey;
	}

	public void setOldLocalUniqueKey(int oldLocalUniqueKey) {
		this.oldLocalUniqueKey = oldLocalUniqueKey;
	}

	public void setRevision(String revision) {
		this.revision = revision;
	}

	public void setModified(Boolean modified) {
		this.modified = modified;
	}

	public void setToBeRemoved(Boolean toBeRemoved) {
		this.toBeRemoved = toBeRemoved;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public void setInqueue(String inqueue) {
		this.inqueue = inqueue;
	}

	public void setSubmittime(String submittime) {
		this.submittime = submittime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public void setScriptFile(String scriptFile) {
		this.scriptFile = scriptFile;
	}

	public void setUserGroup(String userGroup) {
		this.userGroup = userGroup;
	}

	public void setParallelEnvironment(String parallelEnvironment) {
		this.parallelEnvironment = parallelEnvironment;
	}

	public void setEnvironmentVariables(List<String> environmentVariables) {
		this.environmentVariables = environmentVariables;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setIsSGEJob(Boolean isSGEJob) {
		this.isSGEJob = isSGEJob;
	}

	public void setIsCondorJob(Boolean isCondorJob) {
		this.isCondorJob = isCondorJob;
	}

	public void setIsLSFBaseJob(Boolean isLSFBaseJob) {
		this.isLSFBaseJob = isLSFBaseJob;
	}

	public void setIsLSFJob(Boolean isLSFJob) {
		this.isLSFJob = isLSFJob;
	}

	public void setIsOpenLavaJob(Boolean isOpenLavaJob) {
		this.isOpenLavaJob = isOpenLavaJob;
	}

	public void setIsPBSJob(Boolean isPBSJob) {
		this.isPBSJob = isPBSJob;
	}

	public void setIsTorqueJob(Boolean isTorqueJob) {
		this.isTorqueJob = isTorqueJob;
	}

	public void setIsPbsProJob(Boolean isPbsProJob) {
		this.isPbsProJob = isPbsProJob;
	}

}
