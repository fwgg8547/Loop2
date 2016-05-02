package com.fwgg8547.loop2.gamebase.layerbase;

import com.fwgg8547.loop2.gamebase.modelbase.*;

public interface OpenglLayer
{
	abstract public void setupPoints();
	abstract public void setupTexture(int id, int name, int uint);
	abstract public void update();
	abstract public void draw(float[] projectionAndview);
	abstract public void setModels(ModelBase model);
	abstract public int getTextureCount();
	abstract public int getLayerPriority();
	abstract public void initialize(int p);
}
