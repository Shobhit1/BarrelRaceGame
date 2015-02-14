/**
 * Class Name: BarrelRaceModel.java
 * @author
 * 
 *  This data model tracks the width and height of the playing field along with
 *  the current position of a ball.
 * 	the ball speed is meters / second. When we draw to the screen,
 *	1 pixel represents 1 meter. That ends up too slow, so multiply
 *	by this number. Bigger numbers speeds things up.
 */


package com.shobhit.example;

import java.util.concurrent.atomic.AtomicReference;

import android.app.Activity;
import android.os.SystemClock;
import android.os.Vibrator;
import android.util.Log;


public class BarrelRaceModel extends Activity{

	//	SharedPreferences settingsPref = getSharedPreferences("difficulty", 0);


	static float pixelsPerMeter = 8;	//this variable is used to update the speed of the ball
	private final int ballRadius;
	private String timeString;
	//	BarrelRaceActivity barrelRaceActivity;// = new BarrelRaceActivity();

	public String getTimeString() {
		return timeString;
	}

	public void setTimeString(String timeString) {
		this.timeString = timeString;
	}

	public long getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(long updatedTime) {
		this.updatedTime = updatedTime;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	private long timeInMilliseconds = 0L;
	private long updatedTime = 0L;
	long startTime = 0L;
	boolean barrelLCompleted = false;
	public boolean barrelRCompleted = false;
	public boolean barrelMCompleted = false;

	public int flagBarrelLeft[] = new int[4];
	public int flagBarrelRight[] = new int[4];
	public int flagBarrelMiddle[] = new int[4];
	public int roundStateChanged[] = new int[3];
	public static boolean alternateAxis = false;

	int[][] matrixTracer = null;

	// making sure that there is synchronize on lock
	// when reading these. need to get the values in pairs.
	public float ballPixelX, ballPixelY;

	private int pixelWidth, pixelHeight;

	public int getPixelWidth() {
		return pixelWidth;
	}

	public void setPixelWidth(int pixelWidth) {
		this.pixelWidth = pixelWidth;
	}

	public int getPixelHeight() {
		return pixelHeight;
	}

	public void setPixelHeight(int pixelHeight) {
		this.pixelHeight = pixelHeight;
	}

	// in meters/second
	private float velocityX, velocityY;

	// typical values range from -10...10, but could be higher or lower if
	// the user moves the phone rapidly
	private float accelValueX, accelValueY;

	/**
	 * rebound is used to give the ball bounce effect on hitting 
	 * 	the wall.
	 * 0.4f is good value for it. putting 1 in it makes the ball bounce too much.
	 *
	 **/
	private static final float rebound = 0.4f;

	// if the ball bounces and the velocity is less than this constant,
	// stop bouncing.
	private static final float stop_bouncing = 2f;

	private volatile long lastTimeMs = -1;

	public final Object lock = new Object();
	// private BouncingBallActivity bounceActivity;
	private static int BOTTOM_PADDING;

	private boolean bouncedX = false;
	private boolean bouncedY = false;

	private AtomicReference<Vibrator> vibratorRef = new AtomicReference<Vibrator>();

	/**
	 * @return timeString
	 */


	public BarrelRaceModel(int ballRadius) {
		this.ballRadius = ballRadius;
	}

	public void setAccel(float ax, float ay) {
		synchronized (lock) {
			this.accelValueX = ax;
			this.accelValueY = ay;
		}
	}

	public void setSize(int width, int height) {
		synchronized (lock) {
			this.pixelWidth = width;
			this.pixelHeight = height;
			matrixTracer = new int[width + 100][height + 100];
		}
	}

	public int getBallRadius() {
		return ballRadius;
	}

	/**
	 * this functions is used to move the ball
	 * Accelerator values is not updated so that the velocity 
	 * comes down
	 */
	public void moveBall(int ballX, int ballY) {
		synchronized (lock) {
			this.ballPixelX = ballX;
			this.ballPixelY = ballY;
			velocityX = 0;
			velocityY = 0;
		}
	}

	/**
	 * Function checking whether the user has successfully completed the task or not
	 * and then performs the required operations
	 * @return
	 */
	public boolean isUserLost() {


		double distLX = BarrelRaceActivity.barrelLeftX - ballPixelX;
		double distLY = BarrelRaceActivity.barrelLeftY - ballPixelY;


		//		double distLX = barrelRaceActivity.barrelLeftX - ballPixelX;
		//		double distLY = barrelRaceActivity.barrelLeftY - ballPixelY;
		double distFromLeftBarrel = distLX * distLX + distLY * distLY;

		double distRX = BarrelRaceActivity.barrelRightX - ballPixelX;
		double distRY = BarrelRaceActivity.barrelRightY - ballPixelY;

		//		double distRX = barrelRaceActivity.barrelRightX - ballPixelX;
		//		double distRY = barrelRaceActivity.barrelRightY - ballPixelY;
		double distFromRightBarrel = distRX * distRX + distRY * distRY;

		double distMX = BarrelRaceActivity.barrelMiddleX - ballPixelX;
		double distMY = BarrelRaceActivity.barrelMiddleY - ballPixelY;


		//		double distMX = barrelRaceActivity.barrelMiddleX - ballPixelX;
		//		double distMY = barrelRaceActivity.barrelMiddleY - ballPixelY;
		double distFromMiddleBarrel = distMX * distMX + distMY * distMY;

		int sumOfRadius = ballRadius + BarrelRaceActivity.BARREL_RADIUS;
		sumOfRadius = sumOfRadius * sumOfRadius;
		if (sumOfRadius >= distFromLeftBarrel
				|| sumOfRadius >= distFromMiddleBarrel
				|| sumOfRadius >= distFromRightBarrel) {

			moveBall((int) ballPixelX + 1, (int) ballPixelY + 1); // stop the
			// ball
			Vibrator v = vibratorRef.get();
			if (v != null) {
				v.vibrate(20L);
			}
			return true;
		} else
			return false;

	}
	
	/**
	 * Function to update the variables from the accelerometer
	 */
	@SuppressWarnings("all")
	public void updatePhysics() {
		// copying everything to local variables
		if ((accelValueX < 0 & velocityX > 0) || (accelValueX > 0 & velocityX < 0)) {
			accelValueX = accelValueX;
		}
		if ((accelValueY < 0 & velocityY < 0) || (accelValueY > 0 & velocityY > 0)) {
			accelValueY = accelValueY;
		}
		//		Log.d("tag", "inside update1");

		float lWidth, lHeight, lBallX, lBallY, lAx, lAy, lVx, lVy;
		synchronized (lock) {
			BOTTOM_PADDING = BarrelRaceActivity.BOTTOM_PADDING;
			lWidth = pixelWidth;
			lHeight = pixelHeight;
			lBallX = ballPixelX;
			lBallY = ballPixelY;
			lVx = velocityX;
			lVy = velocityY;
			lAx = accelValueX;
			lAy = -accelValueY;
		}
		Log.d("tag", "inside update2");
		if (lWidth <= 0 || lHeight <= 0) {
			// invalid width and height, nothing to do until the GUI comes up
			return;
		}

		long curTime = System.currentTimeMillis();
		if (lastTimeMs < 0) {
			lastTimeMs = curTime;
			return;
		}

		long elapsedMs = curTime - lastTimeMs;
		lastTimeMs = curTime;

		//		// Code for circle
		//		int integerx = (int) Math.ceil(lBallX);
		//		int integery = (int) Math.ceil(lBallY);
		////		Log.d("tag", "inside update3" + integerx + integery);



		// update the velocity
		// result is meters / second
		lVx += ((elapsedMs * lAx) / 1000) * pixelsPerMeter;
		lVy += ((elapsedMs * lAy) / 1000) * pixelsPerMeter;

		// update the position
		// (velocity is meters/sec, so divide by 1000 again)
		lBallX += ((lVx * elapsedMs) / 1000) * pixelsPerMeter;
		lBallY += ((lVy * elapsedMs) / 1000) * pixelsPerMeter;

		bouncedX = false;
		bouncedY = false;

		if (lBallY - ballRadius < 0) {
			lBallY = ballRadius;
			lVy = -lVy * rebound;
			bouncedY = true;
		}

		else if (lBallY + ballRadius > (lHeight - BOTTOM_PADDING)) {
			lBallY = lHeight - ballRadius - BOTTOM_PADDING;
			lVy = -lVy * rebound;
			bouncedY = true;
		}
		if (bouncedY && Math.abs(lVy) < stop_bouncing) {
			lVy = 0;
			bouncedY = false;
		}

		if (lBallX - ballRadius < 0) {
			lBallX = ballRadius;
			lVx = -lVx * rebound;
			bouncedX = true;
		} else if (lBallX + ballRadius > lWidth) {
			lBallX = lWidth - ballRadius;
			lVx = -lVx * rebound;
			bouncedX = true;
		}
		if (bouncedX && Math.abs(lVx) < stop_bouncing) {
			lVx = 0;
			bouncedX = false;
		}

		//copy local vars back to object fields
		synchronized (lock) {
			ballPixelX = lBallX;
			ballPixelY = lBallY;
			velocityX = lVx;
			velocityY = lVy;
		}

		if (bouncedX || bouncedY) {
			Vibrator v = vibratorRef.get();
			startTime -= 5000; // 5 seconds delay, when the user touches the wall
			if (v != null) {
				v.vibrate(20L);
			}
		}
		// timer funda
		timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
		updatedTime = timeInMilliseconds;
		int secs = (int) (updatedTime / 1000);
		int mins = secs / 60;
		secs = secs % 60;
		int milliseconds = (int) (updatedTime % 1000);
		timeString = new String("" + mins + ":" + String.format("%02d", secs)
				+ ":" + String.format("%03d", milliseconds));


	}

	public int[] isCompletedCircle() {

		float ballX, ballY;
		float barrelLeftX, barrelLeftY;
		float barrelRightX, barrelRightY;
		float barrelMiddleX, barrelMiddleY;

		synchronized (lock) {

			ballX = ballPixelX;
			ballY = ballPixelY;
			barrelLeftX = BarrelRaceActivity.barrelLeftX;
			barrelLeftY = BarrelRaceActivity.barrelLeftY;
			barrelMiddleX = BarrelRaceActivity.barrelMiddleX;
			barrelMiddleY = BarrelRaceActivity.barrelMiddleY;
			barrelRightX = BarrelRaceActivity.barrelRightX;
			barrelRightY = BarrelRaceActivity.barrelRightY;
		}

		// Logic for left ball to be circled

		if (ballX > (barrelLeftX - 100) && ballX <= barrelLeftX) {

			if (!alternateAxis) {
				flagBarrelLeft[0] = 1;
				alternateAxis = true;
			}

		}
		if (ballX < (barrelLeftX + 100) && ballX > barrelLeftX) {

			if (!alternateAxis) {
				flagBarrelLeft[1] = 1;
				alternateAxis = true;
			}

		}

		if (ballY > (barrelLeftY - 100) && ballY <= barrelLeftY) {

			if (alternateAxis) {
				flagBarrelLeft[2] = 1;
				alternateAxis = false;
			}

		}

		if (ballY < (barrelLeftY + 100) && ballY > barrelLeftY) {

			if (alternateAxis) {
				flagBarrelLeft[3] = 1;
				alternateAxis = false;
			}

		}

		Log.d("tag", "msg " + ballX + " " + ballY + " " + barrelLeftX + " "
				+ barrelLeftY + " " + flagBarrelLeft[0] + " "
				+ flagBarrelLeft[1] + " " + flagBarrelLeft[2] + " "
				+ flagBarrelLeft[3]);

		if (flagBarrelLeft[0] + flagBarrelLeft[1] + flagBarrelLeft[2]
				+ flagBarrelLeft[3] == 4) {
			roundStateChanged[0] = 1;

			flagBarrelLeft[0] = 0;
			flagBarrelLeft[1] = 0;
			flagBarrelLeft[2] = 0;
			flagBarrelLeft[3] = 0;

			flagBarrelRight[0] = 0;
			flagBarrelRight[1] = 0;
			flagBarrelRight[2] = 0;
			flagBarrelRight[3] = 0;

			flagBarrelMiddle[0] = 0;
			flagBarrelMiddle[1] = 0;
			flagBarrelMiddle[2] = 0;
			flagBarrelMiddle[3] = 0;

		}

		// For Right ball to be circled logic

		if (ballX > (barrelRightX - 100) && ballX <= barrelRightX) {

			if (!alternateAxis) {
				flagBarrelRight[0] = 1;
				alternateAxis = true;
			}

		}
		if (ballX < (barrelRightX + 100) && ballX > barrelRightX) {

			if (!alternateAxis) {
				flagBarrelRight[1] = 1;
				alternateAxis = true;
			}

		}

		if (ballY > (barrelRightY - 100) && ballY <= barrelRightY) {

			if (alternateAxis) {
				flagBarrelRight[2] = 1;
				alternateAxis = false;
			}

		}

		if (ballY < (barrelRightY + 100) && ballY > barrelRightY) {

			if (alternateAxis) {
				flagBarrelRight[3] = 1;
				alternateAxis = false;
			}

		}

		/*Log.d("tag", "msg " + ballX + " " + ballY + " " + barrelRightX + " "
				+ barrelRightY + " " + flagBarrelRight[0] + " "
				+ flagBarrelRight[1] + " " + flagBarrelRight[2] + " "
				+ flagBarrelRight[3]);
*/
		if (flagBarrelRight[0] + flagBarrelRight[1] + flagBarrelRight[2]
				+ flagBarrelRight[3] == 4) {
			roundStateChanged[1] = 1;

			flagBarrelLeft[0] = 0;
			flagBarrelLeft[1] = 0;
			flagBarrelLeft[2] = 0;
			flagBarrelLeft[3] = 0;

			flagBarrelRight[0] = 0;
			flagBarrelRight[1] = 0;
			flagBarrelRight[2] = 0;
			flagBarrelRight[3] = 0;

			flagBarrelMiddle[0] = 0;
			flagBarrelMiddle[1] = 0;
			flagBarrelMiddle[2] = 0;
			flagBarrelMiddle[3] = 0;

		}

		// This piece makes sure that the magic ball is a circle.

		if (ballX > (barrelMiddleX - 100) && ballX <= barrelMiddleX) {

			if (!alternateAxis) {
				flagBarrelMiddle[0] = 1;
				alternateAxis = true;
			}

		}
		if (ballX < (barrelMiddleX + 100) && ballX > barrelMiddleX) {

			if (!alternateAxis) {
				flagBarrelMiddle[1] = 1;
				alternateAxis = true;
			}

		}

		if (ballY > (barrelMiddleY - 100) && ballY <= barrelMiddleY) {

			if (alternateAxis) {
				flagBarrelMiddle[2] = 1;
				alternateAxis = false;
			}

		}

		if (ballY < (barrelMiddleY + 100) && ballY > barrelMiddleY) {

			if (alternateAxis) {
				flagBarrelMiddle[3] = 1;
				alternateAxis = false;
			}
		}
		Log.d("tag", "msg " + ballX + " " + ballY + " " + barrelMiddleX + " "
				+ barrelMiddleY + " " + flagBarrelMiddle[0] + " "
				+ flagBarrelMiddle[1] + " " + flagBarrelMiddle[2] + " "
				+ flagBarrelMiddle[3]);

		if (flagBarrelMiddle[0] + flagBarrelMiddle[1] + flagBarrelMiddle[2]
				+ flagBarrelMiddle[3] == 4) {
			roundStateChanged[2] = 1;

			flagBarrelLeft[0] = 0;
			flagBarrelLeft[1] = 0;
			flagBarrelLeft[2] = 0;
			flagBarrelLeft[3] = 0;

			flagBarrelRight[0] = 0;
			flagBarrelRight[1] = 0;
			flagBarrelRight[2] = 0;
			flagBarrelRight[3] = 0;

			flagBarrelMiddle[0] = 0;
			flagBarrelMiddle[1] = 0;
			flagBarrelMiddle[2] = 0;
			flagBarrelMiddle[3] = 0;

		}
		return roundStateChanged;
	}

	public void setVibrator(Vibrator v) {
		vibratorRef.set(v);
	}
}
