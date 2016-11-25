package vn.vanlanguni.ponggame;

import java.awt.Color;

public class SettingsUsername {
	private String userName1, userName2;
	private Color backgroundColor, paddleColor, ballColor;

	public SettingsUsername(){	}

	public SettingsUsername(String userName1, String userName2, Color backgroundColor, Color paddleColor, Color ballColor) {
		super();
		this.userName1 = userName1;
		this.userName2 = userName2;
		this.backgroundColor = backgroundColor;
		this.paddleColor = paddleColor;
		this.ballColor = ballColor;
	}

	public SettingsUsername(String u1, String u2) {
		userName1 = u1;
		userName2 = u2;
	}

	public String getUserName2() {
		return userName2;
	}

	public void setUserName2(String userName2) {
		this.userName2 = userName2;
	}

	public Color getBackgroundColor() {
		return backgroundColor;
	}

	public void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public Color getPaddleColor() {
		return paddleColor;
	}

	public void setPaddleColor(Color paddleColor) {
		this.paddleColor = paddleColor;
	}

	public Color getBallColor() {
		return ballColor;
	}

	public void setBallColor(Color ballColor) {
		this.ballColor = ballColor;
	}

	public String getUserName1() {
		return userName1;
	}

	public void setUserName1(String uname) {
		userName1 = uname;
	}

}