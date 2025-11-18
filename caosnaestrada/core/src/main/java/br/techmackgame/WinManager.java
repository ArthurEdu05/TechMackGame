package br.techmackgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Gdx;

public class WinManager {
    private FitViewport viewport;
    private SpriteBatch batch;
    private Texture winBg;
    private Texture resumeButton;
    private float resumeX, resumeY, resumeW, resumeH;
    private Sound winSound;
    private Music truckSound;
    private Music introSound;

    private boolean win = false;
    private boolean continueNext = false;
    private boolean soundPlayed = false;

    public WinManager(FitViewport viewport, SpriteBatch batch, Texture winBg, Texture resumeButton,
                      float resumeX, float resumeY, float resumeW, float resumeH,
                      Sound winSound, Music truckSound, Music introSound) {
        this.viewport = viewport;
        this.batch = batch;
        this.winBg = winBg;
        this.resumeButton = resumeButton;
        this.resumeX = resumeX;
        this.resumeY = resumeY;
        this.resumeW = resumeW;
        this.resumeH = resumeH;
        this.winSound = winSound;
        this.truckSound = truckSound;
        this.introSound = introSound;
    }

    public void triggerWin() {
        if (win) return;
        win = true;
        // parar sons de jogo
        if (truckSound != null && truckSound.isPlaying()) truckSound.stop();
        if (introSound != null && introSound.isPlaying()) introSound.stop();
        // tocar som de vitória (uma vez)
        if (winSound != null && !soundPlayed) {
            winSound.play(0.5f);
            soundPlayed = true;
        }
    }

    public void update(float delta) {
        if (!win) return;

        if (Gdx.input.justTouched()) {
            Vector3 v = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            float x = v.x;
            float y = v.y;
            if (x >= resumeX && x <= resumeX + resumeW && y >= resumeY && y <= resumeY + resumeH) {
                continueNext = true;
            }
        }
    }

    public void render() {
        if (!win) return;
        if (batch != null && viewport != null) {
            viewport.apply();
            batch.setProjectionMatrix(viewport.getCamera().combined);
            batch.begin();
            float wWidth = viewport.getWorldWidth() * 0.7f;
            float wHeight = viewport.getWorldHeight() * 0.7f;
            float wX = (viewport.getWorldWidth() - wWidth) / 2f;
            float wY = (viewport.getWorldHeight() - wHeight) / 2f;
            if (winBg != null) batch.draw(winBg, wX, wY, wWidth, wHeight);
            if (resumeButton != null) batch.draw(resumeButton, resumeX, resumeY, resumeW, resumeH);
            batch.end();
        }
    }

    public boolean isWin() {
        return win;
    }

    public boolean shouldContinue() {
        return continueNext;
    }

    // não libera textures/sounds: Level/Main continuam responsáveis pelos assets
    public void dispose() {
    }
}
