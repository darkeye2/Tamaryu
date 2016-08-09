package com.tr.engine.obj.state;

public final class GameStateFactory {
	
	private static final int LoginStateID = 0;
	private static final int MenuStateID = 1;
	private static final int GameStateID = 2;
	private static final int MiniGameStateID = 3;
	
	
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
		
		}
		
		
		return null;
	}
	

}