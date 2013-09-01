package com.example.jp.co.yutaro.tanaka.character;

public class Player implements Roll {

	private int hp, power, defend;

	public Player() {
		this.hp = 0;
		this.power = 0;
		this.defend = 0;
	}

	public Player(int hp, int power, int defend) {
		this.hp = hp;
		this.power = power;
		this.defend = defend;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.example.jp.co.yutaro.tanaka.character.Roll#getHp()
	 */
	@Override
	public int getHp() {
		return hp;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.example.jp.co.yutaro.tanaka.character.Roll#setHp(int)
	 */
	@Override
	public void setHp(int hp) {
		this.hp = hp;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.example.jp.co.yutaro.tanaka.character.Roll#getPower()
	 */
	@Override
	public int getPower() {
		return power;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.example.jp.co.yutaro.tanaka.character.Roll#setPower(int)
	 */
	@Override
	public void setPower(int power) {
		this.power = power;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.example.jp.co.yutaro.tanaka.character.Roll#getDefend()
	 */
	@Override
	public int getDefend() {
		return defend;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.example.jp.co.yutaro.tanaka.character.Roll#setDefend(int)
	 */
	@Override
	public void setDefend(int defend) {
		this.defend = defend;
	}

	public int userAttack(Enemy enemy, String contentsTweet) {
		int damage = 0;

		if (contentsTweet.equals("Ohgi")) {
			damage = this.power * 5;
		} else if (contentsTweet.equals("Ichigeki")) {
			damage = this.power * 500;
		} else {
			damage = this.power;
		}

		// �_���[�W�Z�b�g
		enemy.setHp(enemy.getHp() - damage);

		return damage;
	}
}