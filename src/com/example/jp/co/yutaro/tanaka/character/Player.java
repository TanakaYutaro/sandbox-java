package com.example.jp.co.yutaro.tanaka.character;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.User;

public class Player implements Roll {

	private int hp, power, defend;

	/**
	 * コンストラクタ.
	 */
	public Player() {
		this.hp = 0;
		this.power = 0;
		this.defend = 0;
	}

	/**
	 * コンストラクタ.
	 * 
	 * @param hp
	 * @param power
	 * @param defend
	 */
	public Player(int hp, int power, int defend) {
		this.hp = hp;
		this.power = power;
		this.defend = defend;
	}

	/**
	 * コンストラクタ.
	 * 
	 * @throws TwitterException
	 * @throws IllegalStateException
	 */
	public Player(Twitter twitter) {

		User user = null;
		try {
			user = twitter.showUser(twitter.getScreenName());
		} catch (IllegalStateException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		ResponseList<Status> tweetList = null;
		try {
			tweetList = twitter.getUserTimeline(user.getId());
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
		this.hp = tweetList.size();
		this.power = user.getFriendsCount();
		this.defend = user.getFollowersCount();

		try {
			System.out.println("ScreenName : " + twitter.getScreenName());
		} catch (IllegalStateException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (TwitterException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}
	}

	/**
	 * 攻撃.
	 * 
	 * @param enemy
	 * @param contentsTweet
	 * @return
	 */
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

}