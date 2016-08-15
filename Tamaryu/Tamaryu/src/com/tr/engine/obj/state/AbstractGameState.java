package com.tr.engine.obj.state;

import java.util.ArrayList;

import com.tr.engine.gameobject.AbstractGameObject;

public abstract class AbstractGameState {

	protected ArrayList<AbstractGameObject> currentObject = new ArrayList<AbstractGameObject>();

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

	public void update() {

		for (AbstractGameObject go : currentObject) {

		}

	}

	public void draw() {

	}

}
