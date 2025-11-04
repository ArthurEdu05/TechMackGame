package br.techmackgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.Color;

public abstract class Level {
    protected FitViewport viewport;
    protected SpriteBatch spriteBatch;

    protected Player player;
    protected Truck truck;
    protected FallingObject fallingObject;
    protected Array<Texture> objectTextures;

    protected Texture backgroundTexture;
    protected float bgX1, bgX2;
    protected float bgWidthUnits = 8f;
    protected float bgSpeed = 2f;

    protected float spawnTimer = 0f;
    protected float spawnInterval = 3f;

    protected boolean levelComplete = false;

    public Level(FitViewport viewport, SpriteBatch spriteBatch) {
        this.viewport = viewport;
        this.spriteBatch = spriteBatch;

        setupObjects();
        setupBackground();
    }

    protected abstract void setupBackground();
    protected abstract void setupObjects();

    public void update(float delta) {
        player.update(delta);
        truck.update(delta);

        // Impede o player de entrar no caminhão
        if (player.getBounds().overlaps(truck.getBounds())) {
            if (player.getX() < truck.getX()) {
                player.setPosition(truck.getX() - player.getWidth(), player.getY());
            } else {
                player.setPosition(truck.getX() + truck.getWidth(), player.getY());
            }
        }

        // Controle de spawn
        spawnTimer += delta;
        if ((fallingObject == null || !fallingObject.isActive()) && spawnTimer > spawnInterval) {
            spawnTimer = 0f;
            spawnInterval = MathUtils.random(2f, 5f);

            Texture randomTexture = objectTextures.random();
            float startX = truck.getX();
            float startY = truck.getY() + truck.getHeight();

            fallingObject = new FallingObject(randomTexture, startX, startY, 0.5f, 0.5f);
        }

        if (fallingObject != null) {
            fallingObject.update(delta);
            if (fallingObject.isActive() && player.getBounds().overlaps(fallingObject.getBounds())) {
                fallingObject.collect();
            }
        }

        // Fundo rolando
        bgX1 -= bgSpeed * delta;
        bgX2 -= bgSpeed * delta;
        if (bgX1 + bgWidthUnits < 0) bgX1 = bgX2 + bgWidthUnits;
        if (bgX2 + bgWidthUnits < 0) bgX2 = bgX1 + bgWidthUnits;
    }

    public void render() {
        ScreenUtils.clear(Color.BLACK);
        viewport.apply();
        spriteBatch.setProjectionMatrix(viewport.getCamera().combined);

        spriteBatch.begin();

        spriteBatch.draw(backgroundTexture, bgX1, 0, bgWidthUnits, viewport.getWorldHeight());
        spriteBatch.draw(backgroundTexture, bgX2 + bgWidthUnits, 0, -bgWidthUnits, viewport.getWorldHeight());

        player.draw(spriteBatch);
        truck.draw(spriteBatch);
        if (fallingObject != null) fallingObject.draw(spriteBatch);

        spriteBatch.end();
    }

    public boolean isLevelComplete() {
        return levelComplete;
    }

    public void setLevelComplete(boolean complete) {
        this.levelComplete = complete;
    }

    public void dispose() {
        for (Texture t : objectTextures) t.dispose();
        backgroundTexture.dispose();
    }
}