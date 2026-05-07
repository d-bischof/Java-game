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
    private float minInterval = 0.8f;
    private float maxInterval = 1.5f;
    private float lastSpawnX = 0;

    public CactusManager(float groundY, int rightBound) {
        this.cacti = new ArrayList<>();
        this.groundY = groundY;
        this.rightBound = rightBound;
        this.random = new Random();
        this.timeUntilNextSpawn = 1.0f;
    }

    public void update(float dt) {
        Iterator<Cactus> it = cacti.iterator();
        while (it.hasNext()) {
            Cactus c = it.next();
            c.update(dt);
            if (c.X() < 1) {
                it.remove();
            }
        }

        timeUntilNextSpawn -= dt;
        if (timeUntilNextSpawn <= 0) {
            boolean canSpawn = true;
            for (Cactus c : cacti) {
                if (c.X() > rightBound - 20) {
                    canSpawn = false;
                    break;
                }
            }

            if (canSpawn) {
                int height = random.nextInt(2) + 1;
                cacti.add(new Cactus(rightBound, groundY, height));
                timeUntilNextSpawn = minInterval + random.nextFloat() * (maxInterval - minInterval);
            } else {
                timeUntilNextSpawn = 0.1f;
            }
        }
    }

    public void render() {
        for (Cactus c : cacti) {
            c.render();
        }
    }

    public ArrayList<Cactus> getCacti() {
        return cacti;
    }
}
