package com.fwgg8547.loop2.gamebase.util;

public class TextTexUtil
{
	public static final float RI_TEXT_UV_BOX_WIDTH = 0.125f;
  public static final float RI_TEXT_WIDTH = 32.0f;
  public static final float RI_TEXT_SPACESIZE = 20f;
	
	public static enum FontSize{
		SMALL,
		MIDIUM,
		LARGE
	}
	
	public final static int[] l_size = {
		36,29,30,34,25,25,34,33,
		11,20,31,24,48,35,39,29,
		42,31,27,31,34,35,46,35,
		31,27,30,26,28,26,31,28,
		28,28,29,29,14,24,30,18,
		26,14,14,14,25,28,31,0,
		0,38,39,12,36,34,0,0,
		0,38,0,0,0,0,0,0
	};
	
	public static int convertCharToIndex(int c_val)
	{
		int indx = -1;

		// Retrieve the index
		if(c_val>64&&c_val<91) // A-Z
			indx = c_val - 65;
		else if(c_val>96&&c_val<123) // a-z
			indx = c_val - 97;
		else if(c_val>47&&c_val<58) // 0-9
			indx = c_val - 48 + 26;
		else if(c_val==43) // +
			indx = 38;
		else if(c_val==45) // -
			indx = 39;
		else if(c_val==33) // !
			indx = 36;
		else if(c_val==63) // ?
			indx = 37;
		else if(c_val==61) // =
			indx = 40;
		else if(c_val==58) // :
			indx = 41;
		else if(c_val==46) // .
			indx = 42;
		else if(c_val==44) // ,
			indx = 43;
		else if(c_val==42) // *
			indx = 44;
		else if(c_val==36) // $
			indx = 45;

		return indx;
	}
	
	public static float convSizeToScale(FontSize size){
		float scale=3.0f;
		switch(size){
			case SMALL:
				scale = 3.0f;
				break;
			case MIDIUM:
				scale = 6.0f;
				break;
			case LARGE:
				scale = 12.0f;
				break;
		}

		return scale;
	}
	
}
