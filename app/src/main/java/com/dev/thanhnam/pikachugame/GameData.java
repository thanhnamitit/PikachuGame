package com.dev.thanhnam.pikachugame;

public class GameData {
	public String[][] checkOn;
	public String[][] idImage;
	public static final String CONTINUEGAME = "CHIUCHIUCHIU";
	public static final String CLASSIC_MODE = "ALOXOHOHOHOHO";
	public String indexBackGroundPokemon = "indexBackGroundPokemon1",
			indexBackGround = "indexBackGround";
	public String timeRemaining;
	public String live;
	public String helpRemaining;
	public String level;
	public String score;
	public String replay = "replay";

	public GameData(int width, int height) {
		checkOn = new String[width][height];
		idImage = new String[width][height];
		for (int i = 1; i < width - 1; i++) {
			for (int j = 1; j < height - 1; j++) {
				checkOn[i][j] = "checkOn" + i + "" + j;
				idImage[i][j] = "idImage" + i + "" + j;
			}
		}
		timeRemaining = "timeRemaining";
		helpRemaining = "helpRemaing";
		level = "level";
		score = "score";
		live = "live";
	}
}

// when win level, if you finish activity play game, when continue,
// chanceLocation and checkWay will calling forever
