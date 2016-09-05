package com.tr.engine.obj.state;

import java.util.ArrayList;

import com.tr.engine.gameobject.AbstractGameObject;
import com.tr.engine.gameobject.*;

public abstract class AbstractGameState {

	protected ArrayList<AbstractGameObject> currentObjects = new ArrayList<AbstractGameObject>();
	protected ArrayList<AbstractGameObject> toDrawObjects = new ArrayList<AbstractGameObject>();
	protected ArrayList<AbstractGameObject> soundArray = new ArrayList<AbstractGameObject>();
	

	public void init() {

	}

	public void cleanup() {

	}

	public void pause() {

	}

	public void resume() {

	}

	public void handleEvents() {

	}

	public ArrayList<AbstractGameObject> sortAL(ArrayList<AbstractGameObject> A) {
		ArrayList<AbstractGameObject> nL = new ArrayList<AbstractGameObject>();

		int s = A.size();

		for (int i = 0; i < s - 1; i = i + 1) {
			if (A.get(i) instanceof Zone) {
				nL.add(A.get(i));
			}
		}
		for (int i = 0; i < s - 1; i = i + 1) {
			if (A.get(i) instanceof Decoration) {
				nL.add(A.get(i));
			}
		}
		for (int i = 0; i < s - 1; i = i + 1) {
			if (A.get(i) instanceof Prop) {
				nL.add(A.get(i));
			}
		}
		for (int i = 0; i < s - 1; i = i + 1) {
			if (A.get(i) instanceof Actor) {
				nL.add(A.get(i));
			}
		}
		// maybe clear old list and/or trim it?
		return nL;
	}

	// sort must be removed from update, as soon as i got a better location
	public void update() {
		currentObjects = sortAL(currentObjects);

		for (AbstractGameObject go : currentObjects) {
			go.update(0);
		}

	}

	public void draw() {

	}

}
