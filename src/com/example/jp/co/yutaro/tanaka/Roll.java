package com.example.jp.co.yutaro.tanaka;

public class Roll {
	 
	 private int hp, power, defend; 
	 
	 public Roll () {
			this.hp = 0;
		    this.power = 0;
		    this.defend = 0; 
	 }
	 public Roll (int hp, int power, int defend) {
			this.hp = hp;
		    this.power = power;
		    this.defend = defend; 
	 }

	public int getHp() {
		return hp;
	}

	public void setHp(int hp) {
		this.hp = hp;
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	public int getDefend() {
		return defend;
	}

	public void setDefend(int defend) {
		this.defend = defend;
	}

	 	 
	 /**
	  * 敵への攻撃
	  * 
	  * @param Enemy（攻撃相手）
	  * @param contentsTweet（ツイート内容）
	  * 
	  * @return damage
	  */
	public int userAttack(Roll Enemy, String contentsTweet) {
		int damage = 0;
		

		if (contentsTweet.equals("Ohgi") ){
			damage = this.power * 5;
		} else if (contentsTweet.equals("Ichigeki") ){
			damage = this.power * 500;
		} else {
			damage = this.power;
		}
		
		// ダメージセット
		Enemy.setHp(Enemy.hp - damage);
		
		return damage;
	}
	
	 /**
	  * 敵への攻撃
	  * 
	  * @param Enemy（攻撃相手）
	  * @param contentsTweet（ツイート内容）
	  * 
	  * @return damage
	  */
	public int dragonAttack(Roll Enemy) {
		int damage = 0;
		
		// ダメージ計算
		damage = this.power;
		
		// ダメージセット
		Enemy.setHp(Enemy.hp - damage);
		
		return damage;
	}
	 

}