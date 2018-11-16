package hr.algebra.mygdx.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;


public class MyGdxGame extends ApplicationAdapter {

    MyMethods myMethods;

    public MyGdxGame(MyMethods myMethods) {
        this.myMethods = myMethods;
    }


    private SpriteBatch spriteBatch;

    private Texture bucketImg;
    private Texture dropImg;
    private Texture toxicDropImg;
    private Texture backgroundTexture;

    private Sound dropSound;
    private Sound toxicDropSound;
    private Music rainMusic;

    private Rectangle bucket;
    private Array<Rectangle> raindrops;
    private Array<Rectangle> toxicRaindrops;

    private long lastDropTime;
    private long toxicLastDropTime;

    private OrthographicCamera camera;

    private int score;
    private String yourScoreName;
    private String time;
    private int count;
    private long myTime = 0;

    int i = 0;


    @Override
    public void create() {


        score = 0;
        yourScoreName = "score: 0";
        time = "time: 0";
        count = 0;


        spriteBatch = new SpriteBatch();
        bucketImg = new Texture("bucket.png");
        dropImg = new Texture("droplet.png");
        toxicDropImg = new Texture("toxic_droplet.png");
        backgroundTexture = new Texture("backgroundTexture.jpg");

        dropSound = Gdx.audio.newSound(Gdx.files.internal("drop.wav"));
        toxicDropSound = Gdx.audio.newSound(Gdx.files.internal("toxicDrop.ogg"));


        rainMusic = Gdx.audio.newMusic(Gdx.files.internal("rain.mp3"));
        rainMusic.setLooping(true);
        rainMusic.play();


        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        bucket = new Rectangle();
        bucket.width = 64;
        bucket.height = 64;
        bucket.x = 800 / bucket.width / 2;
        bucket.y = 20;

        raindrops = new Array<Rectangle>();
        toxicRaindrops = new Array<Rectangle>();


    }

    @Override
    public void render() {


        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        camera.update();
        spriteBatch.setProjectionMatrix(camera.combined);


        spriteBatch.begin();

        spriteBatch.draw(backgroundTexture, 0, 0);


        spriteBatch.draw(bucketImg, bucket.x, bucket.y);

        for (Rectangle drop : toxicRaindrops) {
            spriteBatch.draw(toxicDropImg, drop.x, drop.y);
        }

        for (Rectangle drop : raindrops) {
            spriteBatch.draw(dropImg, drop.x, drop.y);
        }

        BitmapFont font = new BitmapFont();
        font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        font.draw(spriteBatch, yourScoreName, 25, 450);

        BitmapFont timefont = new BitmapFont();
        timefont.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        font.draw(spriteBatch, time, 725, 450);


        spriteBatch.end();


        if (TimeUtils.timeSinceNanos(myTime) > 1000000000) {
            myTime = TimeUtils.nanoTime();
            count++;
            time = "time: " + count;
        }

        if (count == 60) {
            myMethods.showToast();

        }


        if (Gdx.input.isTouched()) {
            bucket.x = Gdx.input.getX() / 2 - bucket.width / 2;
        }

        if (bucket.x < 0) bucket.x = 0;
        if (bucket.x > 800 - bucket.width) bucket.x = 800 - bucket.width;

        if (TimeUtils.nanoTime() - lastDropTime > 1000000000) spawnRainDrop();
        if (TimeUtils.nanoTime() - toxicLastDropTime > 1000000000) spawnToxicRainDrop();

        Iterator<Rectangle> tIterator = toxicRaindrops.iterator();
        while (tIterator.hasNext()) {
            Rectangle drop = tIterator.next();
            drop.y -= (i + 10) * Gdx.graphics.getDeltaTime();
            i++;
            if (drop.y + drop.height < 0) {
                tIterator.remove();
            }

            bucket.height = 54;
            if (drop.overlaps(bucket)) {
                toxicDropSound.play();
                score--;
                yourScoreName = "score: " + score;
                tIterator.remove();
            }


        }

        Iterator<Rectangle> iterator = raindrops.iterator();
        while (iterator.hasNext()) {
            Rectangle drop = iterator.next();
            drop.y -= 200 * Gdx.graphics.getDeltaTime();

            if (drop.y + drop.height < 0) {
                iterator.remove();
            }
            bucket.height = 54;
            if (drop.overlaps(bucket)) {
                dropSound.play();
                score++;
                yourScoreName = "score: " + score;
                iterator.remove();
            }


        }

    }

    @Override
    public void dispose() {
        spriteBatch.dispose();
        bucketImg.dispose();
        dropSound.dispose();
        toxicDropSound.dispose();
        rainMusic.dispose();

    }

    private void spawnRainDrop() {
        Rectangle drop = new Rectangle();
        drop.width = 64;
        drop.height = 64;
        drop.x = MathUtils.random(0, 800 - drop.width);
        drop.y = 480;

        raindrops.add(drop);

        lastDropTime = TimeUtils.nanoTime();

    }

    private void spawnToxicRainDrop() {
        Rectangle drop = new Rectangle();
        drop.width = 64;
        drop.height = 64;
        drop.x = MathUtils.random(10, 700 - drop.width);
        drop.y = 480;

        toxicRaindrops.add(drop);

        toxicLastDropTime = TimeUtils.nanoTime();

    }


}
