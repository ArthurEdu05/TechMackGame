package br.techmackgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
// ...existing code...
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
// ...existing code...
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public abstract class Level {
    protected FitViewport viewport;
    protected SpriteBatch spriteBatch;

    protected Player player;
    protected Truck truck;
    protected FallingObject fallingObject;
    protected Array<Texture> objectTextures;
    // energia do jogador para este nível
    protected Energy energy;

    protected Texture backgroundTexture;
    protected float bgX1, bgX2;
    protected float bgWidthUnits = 8f;
    protected float bgSpeed = 2f;

    protected float spawnTimer = 0f;
    protected boolean levelComplete = false;

    protected boolean playerStartedMoving = false;
    private float playerStartX; // posição inicial do player
     
    // Elementos de UI
    protected BitmapFont font;
    protected Stage uiStage;
    protected Label energyLabel;
    protected int score = 0; 

    
    public Level(FitViewport viewport, SpriteBatch spriteBatch) {
        this.viewport = viewport;
        this.spriteBatch = spriteBatch;

    // inicializa e UI 
    font = new BitmapFont();
    font.getData().setScale(1f);
    Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

    uiStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
    energyLabel = new Label("Energia: 0%", style);
    energyLabel.setPosition(10, Gdx.graphics.getHeight() - 30); // canto superior esquerdo
    uiStage.addActor(energyLabel);

        setupObjects();
        setupBackground();

        if (player != null) {
            playerStartX = player.getX();
        }
    }

    // Cada Level define seu spawn interval
    protected abstract float getSpawnInterval(); 

    protected abstract void setupBackground();
    protected abstract void setupObjects();

    public void update(float delta) {
    player.update(delta);
    // Atualiza energia se existir
    if (energy != null) energy.update(delta);
    // Atualiza texto da UI com porcentagem (sem usar GlyphLayout)
    if (energy != null && energyLabel != null) {
        float percent = (energy.getEnergy() / energy.getMaxEnergy()) * 100f;
        energyLabel.setText(String.format("Energia: %.0f%%", percent));
    }
    // atualiza estágio da UI
    if (uiStage != null) uiStage.act(delta);
        truck.update(delta);

        // verifica se player andou
        if (!playerStartedMoving && player != null) {
            if (player.getX() != playerStartX) {
                playerStartedMoving = true;
            }
        }

        // impede o player de entrar no caminhão
        if (player.getBounds().overlaps(truck.getBounds())) {
            if (player.getX() < truck.getX()) {
                player.setPosition(truck.getX() - player.getWidth(), player.getY());
            } else {
                player.setPosition(truck.getX() + truck.getWidth(), player.getY());
            }
        }

        // controle de spawn
        spawnTimer += delta;
        if ((fallingObject == null || !fallingObject.isActive()) && spawnTimer > getSpawnInterval()) {
            spawnTimer = 0f;

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

        // fundo só move se player andou
        if (playerStartedMoving) {
            bgX1 -= bgSpeed * delta;
            bgX2 -= bgSpeed * delta;
            if (bgX1 + bgWidthUnits < 0) bgX1 = bgX2 + bgWidthUnits;
            if (bgX2 + bgWidthUnits < 0) bgX2 = bgX1 + bgWidthUnits;
        }
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

        // Desenha UI Stage (labels)
        if (uiStage != null) {
            // uiStage usa coordenadas de pixels; desenha por cima do spriteBatch
            spriteBatch.end();
            uiStage.getViewport().apply();
            uiStage.draw();
            spriteBatch.begin();
        }

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
    if (font != null) font.dispose();
    if (uiStage != null) uiStage.dispose();
    }
}