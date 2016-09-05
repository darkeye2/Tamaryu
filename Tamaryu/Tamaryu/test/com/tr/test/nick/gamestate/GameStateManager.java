package com.tr.test.nick.gamestate;

import java.util.ArrayList;

public class GameStateManager
{
	private ArrayList<AbstractGameState> gameStates;
	private int currentState;
	
	public static final int STARTMENUSTATE = 0;
	public static final int GAMESTATE = 1;
	public static final int LANGUAGESTATE = 2;
	public static final int HELPSTATE = 3;
	public static final int TESTSTATE = 4;
	public static final int INVENTORYTESTSTATE = 5;
	public static final int SOUNDTESTSTATE = 6;
	
	public GameStateManager()
	{
		gameStates = new ArrayList<AbstractGameState>();
		
		currentState = STARTMENUSTATE;
		gameStates.add(new MenuState(this));
		gameStates.add(new LanguageState(this));
		gameStates.add(new LanguageState(this));
		gameStates.add(new LanguageState(this));
		gameStates.add(new TestState(this));
		gameStates.add(new InventoryTestState(this));
		gameStates.add(new SoundTestState(this));
	}
	
	public void setState(int state)
	{
		currentState = state;
		gameStates.get(currentState).init();
	}
	
	public void update()
	{
		gameStates.get(currentState).update();
	}
	
	public void draw(java.awt.Graphics2D g)
	{
		gameStates.get(currentState).draw(g);
	}
	
	public void keyPressed(int k)
	{
		gameStates.get(currentState).keyPressed(k);
	}
	public void keyReleased(int k)
	{
		gameStates.get(currentState).keyReleased(k);
	}
}