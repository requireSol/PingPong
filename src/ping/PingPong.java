/*
 *  Author: Enam Solaimani
 */

package ping;
import processing.core.PApplet;

public class PingPong extends PApplet{

	public static void main(String[] args){
		PApplet.main(new String[]{"ping.PingPong"});
	}

	//walls
	float wallThick = 20;
	//paddle
	float paddleHeight = wallThick*4;
	float paddlePosL;
	float paddlePosR;
	boolean[] paddleLMove = new boolean[2];
	boolean[] paddleRMove = new boolean[2];
	float paddleSpeed;
	//ball
	float ballxPos;
	float ballyPos;
	int ballSize;
	float ballspeed;
	float ballySpeed;
	//Point
	int pointLeft = 0;
	int pointRight = 0;
	//start
	boolean start;
	int[] stonePos;

	public void settings() {
		size(700, 500);
	}

	public void setup() {
		stonePos = new int[5];
		reset();
	}


	//reset methode sets everything to start except points
	void reset() {
		ballxPos = width/2 + 100;
		ballyPos = height/2;
		ballSize= 20;
		ballspeed= 5;
		ballySpeed = 0;
		paddleSpeed = 5;
		paddlePosL = 250-paddleHeight/2;
		paddlePosR = 250-paddleHeight/2;
	}

	//every 0.4 sec
	public void draw() {
		background(250, 250, 250);
		fill(0,0,0);

		rect(width/2 - 50, height/2 -23, 100, 30);
		textAlign(CENTER);
		textSize(20);
		fill(255);
		text("Start", width/2, height/2);
		if (mouseX > width/2 - 50 && mouseX < width/2 - 50 + 150) {
			if (mouseY > height/2 -23 && mouseY <  height/2 -23 + 30) {
				fill(0,0,0);
				rect(width/2 - 50, height/2 -23, 100, 30);
				textAlign(CENTER);
				textSize(20);
				fill(100);
				text("Start", width/2, height/2);
			}
		}
		//Start
		if (start == true) {
			background(250, 250, 250);
			walls(wallThick);
			ball(ballSize); 
			paddle(paddleHeight);
			bricks();
			fill(0,0,0);
		}


		//Points
		fill(255);
		text(pointRight, 700-15, 18);
		text(pointLeft, 15, 18);
	}


	//Event for Start
	public void mousePressed() {

		if (mouseX > width/2 - 50 && mouseX < width/2 - 50 + 150) {
			if (mouseY > height/2 -25 && mouseY <  height/2 - 25 + 30) {
				start = true;
			}
		}
	}

	void bricks() {

		int yPos= 50;
		int xPos = width/2;
		int i = 0;
		fill(0, 0, 0);
		while (i < 5) {
			rect(xPos-5 + stonePos[i], yPos, 10, 40);
			yPos += 90;
			i++;
		}

		yPos = 45;
		//L
		if (ballspeed < 0) {
			for (int j = 0; j < 5; j++) {
				if (ballxPos + ballSize/2 == xPos-5 + stonePos[j]) {  
					if (ballyPos > yPos && ballyPos  < yPos + 40) {
						ballspeed = -ballspeed;
						stonePos[j] += 5;
					}
				}
				yPos += 90;
			}
		}

		//R
		yPos = 45;
		if (ballspeed > 0) {
			for (int j = 0; j < 5; j++) {
				if (ballxPos - ballSize/2 == xPos+5 + stonePos[j]) {  
					if (ballyPos > yPos && ballyPos  < yPos + 40) {
						ballspeed = -ballspeed;
						stonePos[j] -= 5;
					}
				}
				yPos += 90;
			}
		}
	}

	//If keyPressed Event used set paddle(R/L)Move[a] = t;
	public void keyPressed() {
		//right
		if (keyCode == UP) {
			paddleRMove[0] = true;
		}
		if (keyCode == DOWN) {
			paddleRMove[1] = true;
		}
		//left
		if (key == 'w') {
			paddleLMove[0] = true;
		}
		if (key == 's') {
			paddleLMove[1] = true;
		}
	}

	//If keyR Event used set paddle(R/L)Move[a] = f;
	public void keyReleased() {
		//right
		if (keyCode == UP) {
			paddleRMove[0] = false;
		}
		if (keyCode == DOWN) {
			paddleRMove[1] = false;
		}
		//left
		if (key == 'w') {
			paddleLMove[0] = false;
		}
		if (key == 's') {
			paddleLMove[1] = false;
		}
	}

	void walls(float wallThick) {
		// noStroke() kein rand
		fill(0, 0, 0);
		rect(0, 0, width, wallThick);
		rect(0, height-wallThick, width, wallThick);
	}

	//ball func for ball
	void ball(int ballSize) {
		ellipse(ballxPos, ballyPos, ballSize, ballSize);

		ballxPos = ballxPos - ballspeed;

		float paddleTop = paddlePosR;
		float paddleBot = paddlePosR + paddleHeight;

		//look for depending variables and calc hitting area on right paddle + Curveballs
		if (ballxPos + 10 > width - wallThick/2) {
			if (ballyPos > paddleTop && ballyPos < paddleBot) {
				float balldistance = paddleBot - ballyPos;
				float percent = balldistance/paddleHeight;
				ballySpeed += (-2*percent) + (2*(1-percent));
				if (ballspeed < 0) { 
					ballspeed = -ballspeed;
				}
			}
		}

		paddleTop = paddlePosL;
		paddleBot = paddlePosL + paddleHeight;
		//look for depending variables and calc hitting area on left paddle + Curveballs
		if (ballxPos - 10 < wallThick/2) {
			if (ballyPos > paddleTop && ballyPos < paddleBot) {
				float balldistance = paddleBot - ballyPos;
				float percent = balldistance/paddleHeight;
				ballySpeed += (-2*percent) + (2*(1-percent));
				if (ballspeed > 0) {
					ballspeed = -ballspeed;
				}
			}
		}
		//If someone lose calc Point up
		if (ballxPos < -40  || ballxPos > width +40) {
			if (ballxPos < -40) {
				pointRight ++;
			}
			if (ballxPos > width +40) {
				pointLeft ++;
			}
			reset();
		}
		//Hitting walls top and down
		if (wallThick > ballyPos || height - wallThick < ballyPos) {
			ballySpeed *= -1;
		}
		ballyPos = ballyPos + ballySpeed;
	}

	//func for paddles pos / min max Position
	void paddle(float paddleHeight) {
		//left
		rect(0, paddlePosL, wallThick/2, paddleHeight);
		if (!(paddleLMove[0] && paddleLMove[1])) {
			//max methode get maxYLpos
			if (paddleLMove[0])  paddlePosL = max(wallThick, paddlePosL - paddleSpeed);
			//min methode get maxYLpos
			if (paddleLMove[1])  paddlePosL = min(height-(wallThick + paddleHeight), paddlePosL + paddleSpeed);
		}

		//right
		rect(width-wallThick/2, paddlePosR, wallThick/2, paddleHeight);
		if (!(paddleRMove[0] && paddleRMove[1])) {
			//max methode get maxVRpos
			if (paddleRMove[0])  paddlePosR = max(wallThick, paddlePosR - paddleSpeed);
			//min methode get minYRpos
			if (paddleRMove[1])  paddlePosR = min(height-(wallThick + paddleHeight), paddlePosR + paddleSpeed);
		}
	}

}
