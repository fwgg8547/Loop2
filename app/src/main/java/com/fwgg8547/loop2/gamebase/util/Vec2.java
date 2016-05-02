package com.fwgg8547.loop2.gamebase.util;

public class Vec2
{
	public float x, y;
	
	public Vec2(float x, float y){
		this.x = x;
		this.y = y;
	}
	
	public void multiply(float m){
		this.x = this.x*m;
		this.y = this.y*m;
	}
	
	static public float cross(Vec2 v1, Vec2 v2){
		return v1.x*v2.y - v1.y*v2.x;
	}
	
	static public float dot(Vec2 v1, Vec2 v2){
		return v1.x*v2.x + v1.y*v2.y;
	}
	
	static public float square(Vec2 v){
		return v.x*v.x + v.y*v.y;
	}
	
	static public float size(Vec2 v){
		return (float)Math.sqrt(square(v));
	}
	
	public void rotateDeg(float deg){
		double r = Math.toRadians(deg);
		rotateRad(r);
		
	}
	
	public void rotateRad(double rad){
		float s = (float) Math.sin(rad);
		float c = (float) Math.cos(rad);

		float tx = x* c - y * s;
		float ty = x * s + y * c;
		x=tx;y=ty;
	}
}
