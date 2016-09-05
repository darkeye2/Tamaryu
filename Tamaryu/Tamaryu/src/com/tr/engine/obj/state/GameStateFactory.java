package com.tr.engine.obj.state;

public final class GameStateFactory {
	
	private static final int LoginStateID = 0;
	private static final int IslandStateID = 1;
	private static final int DragonProfileStateID = 2;
	private static final int InteractionStateID = 3;
	private static final int MiniGameStateID = 4;
	private static final int ResultStateID = 5;
	private static final int BreedingStateID = 6;
	
	/*
	public static final int STARTMENUSTATE = 0;
	public static final int GAMESTATE = 1;
	public static final int LANGUAGESTATE = 2;
	public static final int HELPSTATE = 3;
	public static final int TESTSTATE = 4;
	public static final int INVENTORYTESTSTATE = 5;
	*/
	
	public static AbstractGameState create(int id){
		
		switch (id){
		case 0:
			break;
			
		case 1:
			break;
			
		case 2:
			break;
			
		case 3:
			break;

		case 4:
			break;
			
		case 5:
			break;
			
		case 6:
			break;
		
		}
		
		
		return null;
	}
	

}