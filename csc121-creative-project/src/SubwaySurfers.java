import processing.core.PApplet;
import processing.event.KeyEvent;

public class SubwaySurfers {
	private Player p;
	private static Spawner s;
	private Environment g;
	private int score;

	private static boolean isGameOver;

	/*
	 * Creates a brand new version of the game
	 */
	public SubwaySurfers() {
		this.p = new Player();
		this.g = new Environment();
		SubwaySurfers.s = new Spawner();
		score = 0;
	}

	/*
	 * Creates a version of the game with the given inputs
	 */
	public SubwaySurfers(Player p, Spawner sp, Environment g, boolean go, int score) {
		this.p = p;
		this.g = g;
		s = sp;
		isGameOver = go;
		this.score = score;
	}

	/**
	 * Renders a picture of the player and obstacles on the window
	 */
	public PApplet draw(PApplet c) {
		// colors the canvas background
		c.background(45, 160, 230);
		// c.lights(); // this is where lights functions go, needs tweaking to work.
		// look at documentation

		s.getAllObstacles().forEach(ob -> ob.draw(c)); // draws all obstacles

		// positions the camera at (x1,y1,z1) looking toward (x2,y2,z2)
		// SSConstants.HEIGHT/2 + (SSConstants.HEIGHT/2 - p.pos.y)/2
		c.camera(p.getPos().getX(), SSConstants.HEIGHT / 2 - (SSConstants.floorLvl - p.getBounds().getbBound()),
				SSConstants.CAMERA_Z, p.getPos().getX(),
				SSConstants.HEIGHT - (SSConstants.floorLvl - p.getBounds().getbBound()), 0, 0, 1, 0);
		g.draw(c);
		p.draw(c);
		
		c.textSize(100);
		c.textFont(SSConstants.font);
		c.text("Score: " + score, p.getPos().getX() - 180, p.getPos().getY() - 800);
		return c;
	}

	/**
	 * Produces an updated world where the player and obstacles move if needed
	 */
	public SubwaySurfers update() {
		if (!isGameOver) {
			p.update();

			s.spawn();

			s.updateObstacles(); // updates all trains

			checkCollision();
			
			score ++;
			
			SSConstants.gameSpd = 20 + (float) (Math.floorDiv(score, 1000) * 0.1);
			
			System.out.println(SSConstants.gameSpd);
			
		}
		
		if (!Sounds.mainTheme.isPlaying() && !isGameOver) {
			Sounds.mainTheme.play();
		}
		
		return new SubwaySurfers(p, s, g, isGameOver, score);
	}

	/**
	 * Handles KeyEvents for the SubwaySurfers game
	 * 
	 * @param kev - the KeyEvent to be processed
	 * @return the updated game
	 */
	public SubwaySurfers keyPressed(KeyEvent kev) {
		p.move(kev);

		if (kev.getKey() == '1') {
			s.addTrain(1, true);
		} else if (kev.getKey() == '2') {
			s.addTrain(2, false);
		} else if (kev.getKey() == '3') {
			s.addTrain(3, false);
		} else if (kev.getKey() == '4') {
			s.addBarrier(1);
		} else if (kev.getKey() == '5') {
			s.addBarrier(2);
		} else if (kev.getKey() == '6') {
			s.addBarrier(3);
		}

		return new SubwaySurfers(p, s, g, isGameOver, score);
	}

	void checkCollision() {
		int pTrack = p.getCurrentTrack();
		if (p.hasCollided(s.getTrainsOn(pTrack), s.getBarriersOn(pTrack))) {
			gameOver();
		}

	}

	/**
	 * Ends the game
	 */
	public static void gameOver() {
		SSConstants.gameSpd = 0;
		SSConstants.playerSprite.isAnimating = false;

		for (int i = 0; i < s.getAllObstacles().size(); i++) {
			IObstacle ob = s.getAllObstacles().get(i);
			if (ob.getType().equals("train"))
				((Train) ob).setVel(new Vector(0, 0, 0));
		}
		
		Sounds.mainTheme.stop();

		isGameOver = true;
		System.out.println("game over");
	}
}
