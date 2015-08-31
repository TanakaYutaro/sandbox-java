package com.example.jp.co.yutaro.tanaka.character;

/**
 * 敵キャラクラス.
 * 
 * @author gain-glory-victory
 * 
 */
public class Enemy implements Roll {
	private int hp, power, defend;

	/**
	 * デフォルトコンストラクタ.
	 */
	public Enemy() {
		this.hp = 0;
		this.power = 0;
		this.defend = 0;
	}

	/**
	 * パラメータ指定コンストラクタ.
	 * 
	 * @param hp
	 * @param power
	 * @param defend
	 */
	public Enemy(int hp, int power, int defend) {
		this.hp = hp;
		this.power = power;
		this.defend = defend;
	}

	@Override
	public int getHp() {
		return hp;
	}

	@Override
	public void setHp(int hp) {
		this.hp = hp;
	}

	@Override
	public int getPower() {
		return power;
	}

	@Override
	public void setPower(int power) {
		this.power = power;
	}

	@Override
	public int getDefend() {
		return defend;
	}

	@Override
	public void setDefend(int defend) {
		this.defend = defend;
	}

	/**
	 * ダメージを与える.
	 * 
	 * @param Enemy
	 * @return
	 */
	public int dragonAttack(Player Enemy) {
		int damage = 0;

		// TODO ダメージロジック
		damage = this.power;

		Enemy.setHp(Enemy.getHp() - damage);

		return damage;
	}

}
