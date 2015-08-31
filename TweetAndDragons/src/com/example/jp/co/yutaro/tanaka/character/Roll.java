package com.example.jp.co.yutaro.tanaka.character;

/**
 * キャラクターインターフェース.
 * 
 * @author gain-glory-victory
 * 
 */
public interface Roll {

	/**
	 * ゲッター.
	 * 
	 * @return
	 */
	public abstract int getHp();

	/**
	 * セッター.
	 * 
	 * @param hp
	 */
	public abstract void setHp(int hp);

	/**
	 * ゲッター.
	 * 
	 * @return
	 */
	public abstract int getPower();

	/**
	 * セッター.
	 * 
	 * @param power
	 */
	public abstract void setPower(int power);

	/**
	 * ゲッター.
	 * 
	 * @return
	 */
	public abstract int getDefend();

	/**
	 * セッター.
	 * 
	 * @param defend
	 */
	public abstract void setDefend(int defend);

}