package com.fwgg8547.loop2;

import com.fwgg8547.loop2.gamebase.sequencerbase.ItemPattern;
import com.fwgg8547.loop2.generater.ResourceFileReader;

public class GameConfig
{
	public static final int COMMONSCROLL = 1;
	public static final int BLOCKSCROLL = 0;
	public static final int HORIZSCROLL = 0;
	public static final int BLOCKDELETE = 0;
	
	public static final int AUTOMOVE = 1; 
	
	public static final float DELETEMERGIN = -50;
	public final static int HITRANGE = 25;

	// Map define
	public static final int MAPHEIGHT=10;
	public static final int MAPOFFSETW=0;
	public static final int MAPINITIALW = 5;
	public static final int MAPWIDTH=MAPINITIALW + MAPOFFSETW*2;
	public static final float LEFTOFFSET=200;
	public static final float BOTTOMOFFSET=50;

	// Bat define
	// see also pattern.txt
	public static final float CENTEROFFSET = 80;
	public static final float WIDTH = 90;
	public static final float DIFF = WIDTH -CENTEROFFSET;
	public static final float HEIGHT = 40;
	public static final float AUTOHITRANGE = 5;
	public static final float RVELOCITY = 5;
	public static final float BATTINITPOSX = WIDTH*2 *3 + LEFTOFFSET;
	public static final float BATTINITPOSY = WIDTH*2 *5 + BOTTOMOFFSET;

	// Block
	public static final float BLOCKWIDTH = 70;
	public static final GameConfig INSTANCE = new GameConfig();

	// scroll
	public static final int BREAKSCROLL = 36;
	public static final float SCROLLVELOCITY = -5f;
	
	private GameConfig(){

	}

	public void initialize() {
	}

}

