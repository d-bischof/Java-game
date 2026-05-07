package Game;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class CactusManager {
    private ArrayList<Cactus> cacti;
    private float groundY;
    private int rightBound;
    private Random random;
    private float timeUntilNextSpawn;

    private float minInterval, maxInterval;
    private final float MIN_INTERVAL_SLOW = 0.8f;
    private final float MAX_INTERVAL_SLOW = 1.5f;
    private final float MIN_INTERVAL_FAST = 1.2f;
    private final float MAX_INTERVAL_FAST = 2.0f;

    private float currentSpeed = 10.0f;
    private final float MAX_SPEED = 30.0f;
    private final float SPEED_INCREASE_RATE = 0.2f;

    private static final int MAX_CLUSTER_SIZE = 3;

    public CactusManager(float groundY, int rightBound) {
        this.cacti = new ArrayList<>();
        this.groundY = groundY;
        this.rightBound = rightBound;
        this.random = new Random();
        this.minInterval = MIN_INTERVAL_SLOW;
        this.maxInterval = MAX_INTERVAL_SLOW;
        this.timeUntilNextSpawn = 1.0f;
    }

    public void update(float dt) {
        if (currentSpeed < MAX_SPEED) {
            currentSpeed += SPEED_INCREASE_RATE * dt;
            if (currentSpeed > MAX_SPEED) currentSpeed = MAX_SPEED;
            Cactus.speed = currentSpeed;

            float speedRatio = (currentSpeed - 10.0f) / (MAX_SPEED - 10.0f);
            if (speedRatio < 0) speedRatio = 0;
            if (speedRatio > 1) speedRatio = 1;
            minInterval = MIN_INTERVAL_SLOW + speedRatio * (MIN_INTERVAL_FAST - MIN_INTERVAL_SLOW);
            maxInterval = MAX_INTERVAL_SLOW + speedRatio * (MAX_INTERVAL_FAST - MAX_INTERVAL_SLOW);
        }

        Iterator<Cactus> it = cacti.iterator();
        while (it.hasNext()) {
            Cactus c = it.next();
            c.update(dt);
            if (c.X() + cactusWidth(c) < 1) {
                it.remove();
            }
        }

        timeUntilNextSpawn -= dt;
        if (timeUntilNextSpawn <= 0) {
            int clusterSize = getClusterSize();
            if (canSpawnCluster(clusterSize)) {
                spawnCluster(clusterSize);
                timeUntilNextSpawn = minInterval + random.nextFloat() * (maxInterval - minInterval);
            } else {
                timeUntilNextSpawn = 0.1f;
            }
        }
    }

    private int cactusWidth(Cactus c) {
        int h = c.getHeight();
        if (h == 1) return 1;
        return 2; // heights 2 and 3 both width 2
    }

    private int getClusterSize() {
        float speedRatio = (currentSpeed - 10.0f) / (MAX_SPEED - 10.0f);
        if (speedRatio < 0) speedRatio = 0;
        if (speedRatio > 1) speedRatio = 1;

        float r = random.nextFloat();
        if (speedRatio < 0.4f) {
            if (r < 0.4f) return 1;
            if (r < 0.8f) return 2;
            return 3;
        } else if (speedRatio < 0.7f) {
            if (r < 0.6f) return 1;
            if (r < 0.85f) return 2;
            return 3;
        } else {
            if (r < 0.7f) return 1;      // slightly more singles at high speed
            if (r < 0.9f) return 2;
            return 3;
        }
    }

    private int getHeightForSpeed() {
        float speedRatio = (currentSpeed - 10.0f) / (MAX_SPEED - 10.0f);
        if (speedRatio < 0) speedRatio = 0;
        if (speedRatio > 1) speedRatio = 1;

        float r = random.nextFloat();
        if (speedRatio < 0.3f) {       // slow
            if (r < 0.5f) return 1;
            if (r < 0.9f) return 2;
            return 3;
        } else if (speedRatio < 0.7f) { // medium
            if (r < 0.3f) return 1;
            if (r < 0.7f) return 2;
            return 3;
        } else {                        // fast
            if (r < 0.2f) return 1;
            if (r < 0.5f) return 2;
            return 3;
        }
    }

    private boolean canSpawnCluster(int clusterSize) {
        int currentX = rightBound;
        for (int i = 0; i < clusterSize; i++) {
            int height = getHeightForSpeed();
            int width = (height == 1) ? 1 : 2;
            for (Cactus c : cacti) {
                if (c.X() < currentX + width && c.X() + cactusWidth(c) > currentX) {
                    return false;
                }
            }
            currentX += width;
        }
        return true;
    }

    private void spawnCluster(int clusterSize) {
        int currentX = rightBound;
        for (int i = 0; i < clusterSize; i++) {
            int height = getHeightForSpeed();
            cacti.add(new Cactus(currentX, groundY, height));
            currentX += (height == 1) ? 1 : 2;
        }
    }

    public void render() {
        for (Cactus c : cacti) c.render();
    }

    public ArrayList<Cactus> getCacti() {
        return cacti;
    }

    public void reset() {
        cacti.clear();
        currentSpeed = 10.0f;
        minInterval = MIN_INTERVAL_SLOW;
        maxInterval = MAX_INTERVAL_SLOW;
        timeUntilNextSpawn = 1.0f;
        Cactus.speed = currentSpeed;
    }
}
