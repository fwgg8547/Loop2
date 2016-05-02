package com.fwgg8547.loop2.gamebase.layerbase;
import java.util.*;

public class LayerComparator implements Comparator<OpenglLayer>
{

	@Override
	public int compare(OpenglLayer p1, OpenglLayer p2)
	{
		int l1 = p1.getLayerPriority();
		int l2 = p2.getLayerPriority();
		
		if(l1>l2){
			return 1;
		} else if(l1==l2){
			return 0;
		}
		return -1;
	}

}
