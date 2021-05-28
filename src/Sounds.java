import java.io.File;
import javax.sound.sampled.Clip;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;

public class Sounds {

	private String soundName;
	private Clip collisionClip, bulletClip, downClip, gameClip, gameOverClip, levelCompleteClip;
	private AudioInputStream audioInputStream = null;

	public Sounds() {

	}

	public void Collision() {
		try {
			soundName = "resources\\sounds\\explosion.wav";
			audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
			collisionClip = AudioSystem.getClip();
			collisionClip.open(audioInputStream);
			collisionClip.start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void BulletSound() {
		try {
			soundName = "resources\\sounds\\bullet.wav";
			audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
			bulletClip = AudioSystem.getClip();
			bulletClip.open(audioInputStream);
			bulletClip.start();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void EnemyDown() {
		try {
			soundName = "resources\\sounds\\enemyHit.wav";
			audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
			downClip = AudioSystem.getClip();
			downClip.open(audioInputStream);
			downClip.start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public Clip GameBackgroundSound() {
		try {
			soundName = "resources\\sounds\\game.wav";
			audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
			gameClip = AudioSystem.getClip();
			gameClip.open(audioInputStream);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return gameClip;
	}

	public void GameOverFail() {
		try {
			soundName = "resources\\sounds\\gameOver.wav";
			audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
			gameOverClip = AudioSystem.getClip();
			gameOverClip.open(audioInputStream);
			gameOverClip.start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void LevelComplete() {
		try {
			soundName = "resources\\sounds\\levelComplete.wav";
			audioInputStream = AudioSystem.getAudioInputStream(new File(soundName).getAbsoluteFile());
			levelCompleteClip = AudioSystem.getClip();
			levelCompleteClip.open(audioInputStream);
			levelCompleteClip.start();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
