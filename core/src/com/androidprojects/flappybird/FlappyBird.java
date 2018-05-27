package com.androidprojects.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import sun.rmi.runtime.Log;

public class FlappyBird extends ApplicationAdapter {
	SpriteBatch batch; // drawing anything in sprites
	Texture background, topTube, bottomTube, gameOver;// Same as image
    ShapeRenderer shapeRenderer;
    Circle birdCircle;
    Rectangle topTubeRectangles[], bottomTubeRectangles[];
    Texture[] birds;
	int flapState;
	float birdY = 0;
	float velocity = 0;
	int gameState = 0;
	float offset;
    float offsetValue[];
    Random randomGenerator = new Random();
    float tubeVelocity = 4;
    float tubeXPosition[];
    int numberOfTubes = 4;
    float distanceBetweenTubes = 0.6f;
    int score = 0;
    int scoringTube = 0;
    BitmapFont font;
    @Override
	public void create () {
		batch = new SpriteBatch();
		background = new Texture("bg.png");
		birds = new Texture[2];
		gameOver = new Texture("GameOver.png");
		birds[0] = new Texture("bird.png");
		birds[1] = new Texture("bird2.png");
		flapState = 0;
		tubeXPosition = new float[4];
		offsetValue = new float[4];
        font = new BitmapFont();
        font.setColor(Color.WHITE);
        font.getData().setScale(5);
		shapeRenderer = new ShapeRenderer();
		topTubeRectangles = new Rectangle[4];
		bottomTubeRectangles = new Rectangle[4];
		topTube = new Texture("toptube.png");
		bottomTube = new Texture("bottomtube.png");
		offset = Gdx.graphics.getHeight() * 3f / 10f;
        startGame();
    }

    private void startGame() {
        birdY = (Gdx.graphics.getHeight() - birds[0].getHeight())/2;
        for (int i = 0; i < numberOfTubes; i++) {
            topTubeRectangles[i] = new Rectangle();
            bottomTubeRectangles[i] = new Rectangle();
            offsetValue[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() / 2 - offset / 2);
            tubeXPosition[i] = Gdx.graphics.getWidth() + topTube.getWidth() + i * Gdx.graphics.getWidth() * distanceBetweenTubes;
        }
        score = 0;
    }

    @Override
	public void render () {
        birdCircle = new Circle();
        batch.begin();
        batch.draw(background, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if( gameState == 1)
        {
            if(tubeXPosition[scoringTube] < Gdx.graphics.getWidth()/2) {
                score++;
                Gdx.app.log("Score", String.valueOf(score));
                if (scoringTube < numberOfTubes -1) {
                    scoringTube++;
                } else {
                    scoringTube = 0;
                }
            }
            for (int i = 0; i < numberOfTubes; i++) {
                if( tubeXPosition[i] < -topTube.getWidth()) {
                    tubeXPosition[i] += Gdx.graphics.getWidth() * numberOfTubes * distanceBetweenTubes;
                    offsetValue[i] = (randomGenerator.nextFloat() - 0.5f) * (Gdx.graphics.getHeight() / 2 - offset / 2);
                } else {
                    tubeXPosition[i] -= tubeVelocity;
                }
                topTubeRectangles[i] = new Rectangle(tubeXPosition[i], Gdx.graphics.getHeight()/2 + offset/2 +offsetValue[i], topTube.getWidth(), topTube.getHeight());
                bottomTubeRectangles[i] = new Rectangle(tubeXPosition[i], 0, bottomTube.getWidth(),Gdx.graphics.getHeight()/2 - offset/2 + offsetValue[i]);
                batch.draw(topTube,tubeXPosition[i], Gdx.graphics.getHeight()/2 + offset/2 +offsetValue[i]);
                batch.draw(bottomTube, tubeXPosition[i], 0, bottomTube.getWidth(),Gdx.graphics.getHeight()/2 - offset/2 + offsetValue[i]);
            }
            if(Gdx.input.isTouched())
            {
                velocity = -20;
            }
            if(birdY>0) {
                velocity++;
                birdY -= velocity;
            } else {
                gameState = 2;
            }
        } else if (gameState == 0){
            // To draw background
            if(Gdx.input.justTouched()){
                gameState = 1;
            }
        } else if(gameState == 2) {
            batch.draw(gameOver, Gdx.graphics.getWidth() * 0.25f, Gdx.graphics.getHeight() * 0.4f, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/5 );
            if (Gdx.input.isTouched()) {
                startGame();
                velocity = 0;
                scoringTube = 0;
                gameState = 1;
            }
        }
        if(flapState == 0) {
            flapState = 1;
        } else {
            flapState = 0;
        }
        font.draw(batch, "Score: " + score, Gdx.graphics.getWidth() * 0.7f, Gdx.graphics.getHeight()*0.95f);
        batch.draw(birds[flapState], (Gdx.graphics.getWidth()-birds[flapState].getWidth())/2, birdY);
        batch.end();
        //shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        //shapeRenderer.setColor(Color.RED);
        birdCircle.set(Gdx.graphics.getWidth()/2, birdY + birds[flapState].getHeight()/2, birds[flapState].getWidth()/2);
        //shapeRenderer.circle(birdCircle.x, birdCircle.y, birdCircle.radius);
        for (int i=0; i < numberOfTubes; i++)
        {
            //shapeRenderer.rect(topTubeRectangles[i].x, topTubeRectangles[i].y, topTubeRectangles[i].width, topTubeRectangles[i].height);
            //shapeRenderer.rect(bottomTubeRectangles[i].x, bottomTubeRectangles[i].y, bottomTubeRectangles[i].width, bottomTubeRectangles[i].height);
            if(Intersector.overlaps(birdCircle, topTubeRectangles[i]) || Intersector.overlaps(birdCircle, topTubeRectangles[i])) {
                gameState = 2;
            }
        }
        //shapeRenderer.end();

    }
	
	@Override
	public void dispose () {
		batch.dispose();
		background.dispose();
		birds[0].dispose();
		birds[1].dispose();
	}
}
