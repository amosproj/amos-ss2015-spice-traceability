package de.fau.osr.gui;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;

import de.fau.osr.bl.Tracker;
import de.fau.osr.core.db.DataSource;
import de.fau.osr.core.vcs.base.CommitFile;
import de.fau.osr.core.vcs.base.VcsController;

public class DataRetriever {
	
	Logger logger = LoggerFactory.getLogger(DataRetriever.class);

	Tracker tracker;
	VcsController vcsController;
	DataSource dataSource;
	
	public DataRetriever(VcsController vcsController,Tracker tracker, DataSource dataSource){
		this.vcsController = vcsController;
		this.tracker = tracker;
		this.dataSource = dataSource;
	}

		/*
	 * added a parameter 'requirementPattern' to easily change the pattern in runtime
	 * have to ask the author of this method to extend the method in the master
	 */
	public List<Integer> parse(String latestCommitMessage,String requirementPattern) {
		final Pattern REQUIREMENT_PATTERN = Pattern.compile(requirementPattern);
		Matcher m = REQUIREMENT_PATTERN.matcher(latestCommitMessage);
		List<Integer> found_reqids = new ArrayList<Integer>();

		while(m.find())  {
			found_reqids.add(Integer.valueOf(m.group(1)));
		}

		return found_reqids;

	}
	
	
	
	/*
	 * Req-4 + Req-5 + Req-6 + Req-7
	 * Responsibility: Flo
	 */
	public ArrayList<CommitFile> getCommitFilesForRequirementID(String requirementID,String requirementPattern){

		ArrayList<CommitFile> commitFilesList = new ArrayList<CommitFile>();
		
			Iterator<String> commits = vcsController.getCommitList();
			
			while(commits.hasNext())
			{
				String currentCommit = commits.next();
				if(parse(vcsController.getCommitMessage(currentCommit),requirementPattern).contains(Integer.valueOf(requirementID)))
				{
					commitFilesList.addAll(vcsController.getCommitFiles(currentCommit));
					
				}
			}
		
		
		return commitFilesList;
	} 
	
	
	/*
	 * Req-13
	 * Responsibility: Taleh
	 */
	public void addRequirementCommitRelation(Integer requirementID, String commitID) {
		try {
			dataSource.addReqCommitRelation(requirementID, commitID);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * Req-13
	 * Responsibility: Taleh
	 */
	public ArrayList<String> getRequirementCommitRelationFromDB(Integer requirementID) {
		// TODO programm to an interface ==> ArrayList to Iterable
		ArrayList<String> rvalue = null;
		try {
			rvalue = Lists.newArrayList(dataSource.getCommitRelationByReq(requirementID));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return rvalue;
	}
	
	/*
	 * Req-8
	 * Responsibility: Gayathery
	 */
	public List<Integer> getRequirementIDsForFile(String filePath){
		
		logger.info("Start call :: getRequirementIDsForFile()"+filePath);
		
		List<Integer> requirementIDlist = new ArrayList<Integer>();
		
		requirementIDlist = tracker.getAllRequirementsforFile(filePath);
		
		return requirementIDlist;
	}

	
	/*
	 * Req-12
	 * Responsibility: Rajab
	 */
	public ArrayList<String> getRequirementIDs(){
		ArrayList<String> requirements = new ArrayList<String>();
		requirements.add("0");
		requirements.add("1");
		requirements.add("2");
		requirements.add("3");
		requirements.add("4");
		requirements.add("5");
		requirements.add("6");
		requirements.add("7");
		return requirements;
	}
}
