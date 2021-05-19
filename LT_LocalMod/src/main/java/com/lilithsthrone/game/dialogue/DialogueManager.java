package com.lilithsthrone.game.dialogue;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.lilithsthrone.game.character.GameCharacter;
import com.lilithsthrone.game.dialogue.utils.UtilText;
import com.lilithsthrone.main.Main;
import com.lilithsthrone.utils.Util;

/**
 * Handles the loading and id generation of DialogueNodes from external files.
 * 
 * @since 0.4
 * @version 0.4
 * @author Innoxia
 */
public class DialogueManager {
	
	private static List<DialogueNode> allDialogues = new ArrayList<>();
	private static Map<DialogueNode, String> dialogueToIdMap = new HashMap<>();
	private static Map<String, DialogueNode> idToDialogueMap = new HashMap<>();

	/**
	 * For use in external res files as a way to get a hook to UtilText.parseFromXMLFile
	 */
	public String getDialogueFromFile(String pathName, String tag, GameCharacter... specialNPCs) {
		return UtilText.parseFromXMLFile(new ArrayList<>(), "res", pathName, tag, Arrays.asList(specialNPCs));
	}
	
	public static List<DialogueNode> getAllDialogues() {
		return allDialogues;
	}
	
	public static DialogueNode getDialogueFromId(String id) {
		if(id==null || id.equalsIgnoreCase("null") || id.equalsIgnoreCase("empty") || id.isEmpty()) {
			return null;
			
		} else if(id.equalsIgnoreCase("default")) {
			return Main.game.getDefaultDialogue(false);
			
		} else if(id.equalsIgnoreCase("defaultWithEncounter")) {
			return Main.game.getDefaultDialogue(true);
			
		} else if(id.equalsIgnoreCase("defaultForceEncounter")) {
			DialogueNode dn = Main.game.getDefaultDialogue(true, true);
//			System.out.println("Manager returning: "+dn.getId());
			return dn;
			
		}
		
		id = Util.getClosestStringMatch(id, idToDialogueMap.keySet());
		return idToDialogueMap.get(id);
	}
	
	public static String getIdFromDialogue(DialogueNode placeType) {
		return dialogueToIdMap.get(placeType);
	}
	
	static {
//		// Modded dialogue types:
//		
//		Map<String, Map<String, File>> moddedFilesMap = Util.getExternalModFilesById("/maps", null, "dialogue");
//		for(Entry<String, Map<String, File>> entry : moddedFilesMap.entrySet()) {
//			for(Entry<String, File> innerEntry : entry.getValue().entrySet()) {
//				try {
//					DialogueNode dialogue = new DialogueNode(innerEntry.getValue(), entry.getKey(), true) {};
//					allDialogues.add(dialogue);
//					dialogueToIdMap.put(dialogue, innerEntry.getKey());
//					idToDialogueMap.put(innerEntry.getKey(), dialogue);
////					System.out.println("modded WT: "+innerEntry.getKey());
//				} catch(Exception ex) {
//					System.err.println("Loading modded dialogue type failed at 'Dialogue'. File path: "+innerEntry.getValue().getAbsolutePath());
//					System.err.println("Actual exception: ");
//					ex.printStackTrace(System.err);
//				}
//			}
//		}
		
		// External res dialogue types:
		
		Map<String, Map<String, File>> filesMap = Util.getExternalFilesById("res/dialogue");
		for(Entry<String, Map<String, File>> entry : filesMap.entrySet()) {
			for(Entry<String, File> innerEntry : entry.getValue().entrySet()) {
				if(Util.getXmlRootElementName(innerEntry.getValue()).equals("dialogueNodes")) {
					try {
						List<DialogueNode> nodes = DialogueNode.loadDialogueNodesFromFile(innerEntry.getKey(), innerEntry.getValue(), entry.getKey(), false);
//						System.out.println("size: "+nodes.size());
						for(DialogueNode node : nodes) {
//							System.out.println("res dialogue: "+node.getId());
							allDialogues.add(node);
							dialogueToIdMap.put(node, node.getId());
							idToDialogueMap.put(node.getId(), node);
						}
					} catch(Exception ex) {
						System.err.println("Loading dialogue type failed at 'Dialogue'. File path: "+innerEntry.getValue().getAbsolutePath());
						System.err.println("Actual exception: ");
						ex.printStackTrace(System.err);
					}
				}
			}
		}
	}
	
}
