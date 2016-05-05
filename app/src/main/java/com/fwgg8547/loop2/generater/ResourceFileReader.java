package com.fwgg8547.loop2.generater;

import com.fwgg8547.loop2.GameConfig;
import com.fwgg8547.loop2.gamebase.util.Vec2;
import com.fwgg8547.loop2.gamebase.sequencerbase.MotionSequnce;
import com.fwgg8547.loop2.gamebase.sequencerbase.*;
import com.fwgg8547.loop2.R;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.*;
import java.io.InputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import org.json.*;
import com.fwgg8547.loop2.gamebase.modelbase.*;
import com.fwgg8547.loop2.model.*;

public class ResourceFileReader
{
	private final static String TAG = ResourceFileReader.class.getSimpleName();
	public final static int BLOCKIDX = 0;
	public final static int BACKIDX = BLOCKIDX +1;
	public final static int BALLIDX = BACKIDX +1;
	public final static int BATTIDX = BALLIDX +1;
	public final static int WALLIDX = BATTIDX +1;
	
	public enum Type {
		Block,
		Back,
		Ball,
		Batt,
		Wall,
	}
	
	private static Context mContext;
	private static List<MotionSequnce[]> mMotionPatternArray;
	private static List<RotateSequence[]> mRotatePatternArray;
	private static List<ScaleSequence[]> mScalePatternArray;
	private static List<TextureSequence[]> mTexturePatternArray;
	private static int[] mItemIdPattern;
	private static ItemPattern[][] mItemPattern;
	private static int[] mLevelThresh;
	private static List<ItemGeneraterBase.Sequence[]>mSqArray;
	
	public ResourceFileReader(Context ctx){
		mContext = ctx;	
	}
	
	public static void clear(){
		mContext = null;
		if(mMotionPatternArray != null){
			mMotionPatternArray.clear();
			mMotionPatternArray = null;
		}
		if(mRotatePatternArray != null){
			mRotatePatternArray.clear();
			mRotatePatternArray = null;
		}
		if(mScalePatternArray != null){
			mScalePatternArray.clear();
			mScalePatternArray = null;
		}
		if(mTexturePatternArray != null){
			mTexturePatternArray.clear();
			mTexturePatternArray = null;
		}
		mItemPattern = null;
		mLevelThresh = null;
		
		if(mSqArray != null){
			mSqArray.clear();
			mSqArray = null;
		}
	}
	
	public static ItemPattern getPattern(Type t, int id){
		switch(t){
			case Ball:
				return mItemPattern[BALLIDX][id];
			case Block:
				return mItemPattern[BLOCKIDX][id];
			case Back:
				return mItemPattern[BACKIDX][id];
			case Batt:
				return mItemPattern[BATTIDX][id];
			case Wall:
				return mItemPattern[WALLIDX][id];
			default:
				return null;
		}
	}
	
	public static ItemGeneraterBase.Sequence[] getSequence(int l){
		if(mSqArray != null && l >= 0){
			return mSqArray.get(l);
		}
		return null;
	}
	
	public static int getSequenceSize(){
		return mSqArray.size();
	}
	
	public static int[] getLevelThreash(){
		return mLevelThresh;
	}
	
	public static MotionSequnce[] getMotionPattern(int id){
		return mMotionPatternArray.get(id);
	}
	
	public void loadSequence(){
		Resources r = mContext.getResources();
		InputStream is = r.openRawResource(R.raw.sequence);
		try {
			int size = is.available();
			byte[] data = new byte[size];
			is.read(data);
			is.close();

			String json = new String(data);
			JSONObject obj = new JSONObject(json);

			JSONArray array = obj.getJSONArray("level");
			mLevelThresh = new int[array.length()];
			for(int i=0; i<array.length();i++){
				mLevelThresh[i] = array.getInt(i);
			}
			
			mSqArray = new ArrayList<ItemGeneraterBase.Sequence[]>();
			array = obj.getJSONArray("sequence");
			for(int i=0; i<array.length();i++){
				// level
				JSONArray inner = array.getJSONArray(i);
				ItemGeneraterBase.Sequence[] seq = new ItemGeneraterBase.Sequence[inner.length()/4];
				for(int j=0, k=0; j<inner.length();j+=4){
					seq[k]  = new ItemGeneraterBase.Sequence(
					inner.getInt(j),
					Integer.parseInt(inner.getString(j+1), 16),
					Integer.parseInt(inner.getString(j+2), 16),
					inner.getInt(j+3));
					k++;
				}
				mSqArray.add(seq);
			}
			
		}catch(Exception e){

		}
	}
	
	public ItemPattern[][] loadPattern(){
		mMotionPatternArray = loadMotionSequence();
		mRotatePatternArray = loadRotateSequence();
		mScalePatternArray = loadScaleSequence();
		mTexturePatternArray = loadTextureSequence();
		
		Resources r = mContext.getResources();
		InputStream is = r.openRawResource(R.raw.pattern);
		ItemPattern[] block=null,ball=null,back=null,batt=null,wall=null;
		
		try {
			int size = is.available();
			byte[] data = new byte[size];
			is.read(data);
			is.close();

			String json = new String(data);
			JSONObject obj = new JSONObject(json);
			
			JSONArray array = obj.getJSONArray("Block");
			if(array != null){
				//block = getItemPattern(array);
				block = getItemPatternBlock();
			}
			
			array = obj.getJSONArray("Ball");
			if(array != null){
				ball = getItemPattern(array);
			}
			
			array = obj.getJSONArray("Back");
			if(array != null){
				back = getItemPattern(array);
			}
			
			array = obj.getJSONArray("Batt");
			if(array != null){
				batt = getItemPattern(array);
				
			}
			
			array = obj.getJSONArray("Wall");
			if(array != null){
				wall = getItemPatternWall(array);
			}
			
		}catch(Exception e){
			
		}finally{
			mItemPattern = new ItemPattern[][] {
				block,
				back,
				ball,
				batt,
				wall
			};
		}
		return mItemPattern;
	}

	private ItemPattern[] getItemPatternBlock(){
		ItemPattern[] ip = new ItemPattern[GameConfig.MAPHEIGHT*GameConfig.MAPWIDTH];
		float blocksize = GameConfig.BLOCKWIDTH;
		float battwidth = GameConfig.WIDTH*2;
		float pointsize = GameConfig.CENTEROFFSET*2;
		float offset = battwidth*GameConfig.MAPOFFSETW*-1;
		int indx=0;
		for(int i=0; i<GameConfig.MAPHEIGHT; i++){
			for(int j = -GameConfig.MAPOFFSETW; j< GameConfig.MAPWIDTH - GameConfig.MAPOFFSETW; j++){
				ip[indx] = new ItemPattern();
				ip[indx].mInitPos = new PointF(j*battwidth+GameConfig.LEFTOFFSET, i*battwidth+GameConfig.BOTTOMOFFSET);
				ip[indx].mMotionPattern = null;//mMotionPatternArray.get(1);
				ip[indx].mRotatePattern = null;
				ip[indx].mScalePattern = null;
				ip[indx].mTexturePattern = mTexturePatternArray.get(2);
				ip[indx].mRect = new RectF(
				-blocksize/2f,-blocksize/2f,
				 blocksize/2f, blocksize/2f);
				indx++;
			}
		}
		
		return ip;
	}
	
	private ItemPattern[] getItemPatternWall(JSONArray array) {
		ItemPattern[] ip = new ItemPattern[array.length()];
		try {
			for(int i=0; i < array.length(); i++) {
				ip[i] = new ItemPattern();
				JSONArray inner = array.getJSONArray(i);
				// position, rect, motionsequence, scalesequence, texturesequnce, textureid
				try{
					ip[i].mInitPos = new PointF(
						(float)inner.getDouble(0), 
						(float)inner.getDouble(1));
				} catch (Exception e) {
					ip[i].mInitPos = null;
				}

				try{
					ip[i].mMotionPattern = mMotionPatternArray.get(
						inner.getInt(6));
				} catch (Exception e) {
					ip[i].mMotionPattern = null;
				}

				try{
					ip[i].mScalePattern = mScalePatternArray.get(
						inner.getInt(7));
				} catch (Exception e) {
					ip[i].mScalePattern = null;
				}

				try {
					ip[i].mTexturePattern = mTexturePatternArray.get(
						inner.getInt(8));
				} catch (Exception e) {
					ip[i].mTexturePattern = null;
				}

				try {
					if((inner.getInt(9)%2) == 1){
						ip[i].mType = 1; // right
					} else {
						ip[i].mType = 0; // left
					}
					ip[i].mItemId = inner.getInt(9);
				} catch (Exception e) {
					ip[i].mItemId = 0;
				}
				
				try{
					Quadrilateral q = null;
					if(ip[i].mType == 1) {
						q = new Quadrilateral(
							new PointF((float)inner.getDouble(2),(float)inner.getDouble(3)),
							new PointF(1080, (float)inner.getDouble(3)),
							new PointF(1080,(float)inner.getDouble(5)),
							new PointF((float)inner.getDouble(4),(float)inner.getDouble(5))
						);
					} else {
						q = new Quadrilateral(
							new PointF(0,(float)inner.getDouble(3)),
							new PointF((float)inner.getDouble(2),(float)inner.getDouble(3)),
							new PointF((float)inner.getDouble(4),(float)inner.getDouble(5)),
							new PointF(0, (float)inner.getDouble(5))
						);
					}
					ip[i].mQuad = q;
				} catch (Exception e) {
					ip[i].mQuad = null;
					ip[i].mRect = null;
				}
			}
		} catch(Exception e){
	
		} finally {
		
		}
		
		return ip;
	}
	
	private ItemPattern[] getItemPattern(JSONArray array) {
		ItemPattern[] ip = new ItemPattern[array.length()];
		try {
			for(int i=0; i < array.length(); i++) {
				ip[i] = new ItemPattern();
				JSONArray inner = array.getJSONArray(i);
				// position, rect, motionsequence, scalesequence, texturesequnce, textureid
				try{
					ip[i].mInitPos = new PointF(
						(float)inner.getDouble(0), 
						(float)inner.getDouble(1));

				} catch (Exception e) {
					ip[i].mInitPos = null;
				}

				try{
					ip[i].mRect = new RectF(
						(float)inner.getDouble(2),
						(float)inner.getDouble(3),
						(float)inner.getDouble(4),
						(float)inner.getDouble(5));
				} catch (Exception e) {
					ip[i].mRect = null;
				}

				try{
					ip[i].mMotionPattern = mMotionPatternArray.get(
						inner.getInt(6));
				} catch (Exception e) {
					ip[i].mMotionPattern = null;
				}
				
				try{
					ip[i].mRotatePattern = mRotatePatternArray.get(
						inner.getInt(7));
				} catch (Exception e) {
					ip[i].mRotatePattern = null;
				}

				try{

					ip[i].mScalePattern = mScalePatternArray.get(
						inner.getInt(8));
				} catch (Exception e) {
					ip[i].mScalePattern = null;
				}

				try {
					ip[i].mTexturePattern = mTexturePatternArray.get(
						inner.getInt(9));
				} catch (Exception e) {
					ip[i].mTexturePattern = null;
				}

				try {
					/*
					 ip[i].mTextureId = mContext.getResources().getIdentifier(
					 inner.getString(9), "drawable", mContext.getPackageName());
					 */
					ip[i].mItemId = inner.getInt(10);
				} catch (Exception e) {

					ip[i].mItemId = 0;
				}
			}
		} catch(Exception e){

		} finally {

		}

		return ip;
	}
	
	public List<MotionSequnce[]> loadMotionSequence(){
		ArrayList<MotionSequnce[]> msl = new ArrayList<MotionSequnce[]>();
		Resources r = mContext.getResources();
		InputStream is = r.openRawResource(R.raw.motionsequence);
		try {
			int size = is.available();
			byte[] data = new byte[size];
			is.read(data);
			is.close();
			
			String json = new String(data);
			JSONObject obj = new JSONObject(json);
			
			JSONArray array = obj.getJSONArray("Sequence");
			for(int i=0; i < array.length(); i++) {
				JSONArray inner = array.getJSONArray(i);
				MotionSequnce[] ms = new MotionSequnce[inner.length()];
				
				for(int j=0; j < inner.length(); j++){
					ms[j] = new MotionSequnce();
					JSONArray elem = inner.getJSONArray(j);
					ms[j].frame = elem.getInt(0);
					ms[j].ppf = (float)elem.getDouble(1);
					float vx =  (float)elem.getDouble(2);
					float vy =  (float)elem.getDouble(3);
					ms[j].direct = new Vec2(vx, vy);
				}				
				msl.add(ms);
			}
		} catch(Exception e){
			
		} finally {
			
		}
		return msl;
	}
	
	public List<RotateSequence[]> loadRotateSequence(){
		ArrayList<RotateSequence[]> msl = new ArrayList<RotateSequence[]>();
		Resources r = mContext.getResources();
		InputStream is = r.openRawResource(R.raw.rotatesequence);
		try {
			int size = is.available();
			byte[] data = new byte[size];
			is.read(data);
			is.close();

			String json = new String(data);
			JSONObject obj = new JSONObject(json);

			JSONArray array = obj.getJSONArray("Sequence");
			for(int i=0; i < array.length(); i++) {
				JSONArray inner = array.getJSONArray(i);
				RotateSequence[] ms = new RotateSequence[inner.length()];

				for(int j=0; j < inner.length(); j++){
					ms[j] = new RotateSequence();
					JSONArray elem = inner.getJSONArray(j);
					ms[j].frame = elem.getInt(0);
					ms[j].dpf = (float)elem.getDouble(1);
				}				
				msl.add(ms);
			}
		} catch(Exception e){

		} finally {

		}
		return msl;
	}
	
	public List<ScaleSequence[]> loadScaleSequence(){
		ArrayList<ScaleSequence[]> msl = new ArrayList<ScaleSequence[]>();
		Resources r = mContext.getResources();
		InputStream is = r.openRawResource(R.raw.scalesequence);
		try {
			int size = is.available();
			byte[] data = new byte[size];
			is.read(data);
			is.close();

			String json = new String(data);
			JSONObject obj = new JSONObject(json);

			JSONArray array = obj.getJSONArray("Sequence");
			for(int i=0; i < array.length(); i++) {
				JSONArray inner = array.getJSONArray(i);
				ScaleSequence[] ms = new ScaleSequence[inner.length()];
				
				for(int j=0; j < inner.length(); j++){
					ms[j] = new ScaleSequence();
					JSONArray elem = inner.getJSONArray(j);
					ms[j].frame = elem.getInt(0);
					ms[j].scalex = (float)elem.getDouble(1);
					ms[j].scaley = (float)elem.getDouble(2);
				}				
				msl.add(ms);
			}
		} catch(Exception e){

		} finally {

		}
		return msl;
	}
	
	private List<TextureSequence[]> loadTextureSequence(){
		ArrayList<TextureSequence[]> msl = new ArrayList<TextureSequence[]>();
		TextureSequence[] mTx;
		
		float [][] uv;
		uv = makeUvPattern(1);
		mTx = new TextureSequence[] {
			new TextureSequence(1, uv[0]),
		};
		msl.add(mTx);
			
		uv = makeUvPattern(2);
		mTx = new TextureSequence[] {
			new TextureSequence(30, uv[0]),
			new TextureSequence(30, uv[1]),
			new TextureSequence(30, uv[2]),
			new TextureSequence(30, uv[3]),
		};
		msl.add(mTx);
		
		uv = makeUvPattern(3);
		mTx = new TextureSequence[] {
			new TextureSequence(30, uv[0]),
			new TextureSequence(30, uv[1]),
			new TextureSequence(30, uv[2]),
			new TextureSequence(30, uv[3]),
			new TextureSequence(30, uv[4]),
			new TextureSequence(30, uv[5]),
			new TextureSequence(30, uv[6]),
			new TextureSequence(30, uv[7]),
			new TextureSequence(30, uv[8]),
		};
		msl.add(mTx);
		
		return msl;
	}
	
	// x,y  x,y+d, x+d,y+d, x+d,y
	private float[][] makeUvPattern(int divNumX){
		if(divNumX <= 0){
			return null;
		}

		float offset = 1f/(float)divNumX;
		float[][] out = new float[divNumX*divNumX][8];
		for(int x=0,i=0; x<divNumX; x++){
			for(int y=0;y<divNumX; y++){

				out[i][0] = (float)x*offset; //x
				out[i][1] = (float)y*offset + offset; //y +d
				
				out[i][2] = (float)x*offset; //x
				out[i][3] = (float)y*offset; //y
				
				out[i][4] = (float)x*offset + offset; //x+d
				out[i][5] = (float)y*offset; //y
				
				out[i][6] = (float)x*offset + offset; //x+d
				out[i][7] = (float)y*offset + offset; //y

				i++;
			}
		}

		return out;
	}
	
}
