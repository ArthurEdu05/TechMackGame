package br.techmackgame;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.Gdx;


/**
 * Esta classe não é uma tela mas sim um gerenciador que assume o controle do Render e do Update quando o jogador perde.
 * Parar a música do jogo e caminhão; dar feedback auditivo e visual de derrota; Detectar retorno ao menu.
 */
public class GameOverManager {
    private FitViewport viewport;
    private SpriteBatch batch;
    private Texture gameOverBg;
    private Texture exitButton;
    private float exitX, exitY, exitW, exitH;
    private Sound gameOverSound;
    private Music truckSound;
    private Music introSound;

    private boolean gameOver = false;
    private boolean exitGameOver = false;
    private boolean soundPlayed = false;

    //recebe todos os assets já carregados pela classe Level.
    public GameOverManager(FitViewport viewport, SpriteBatch batch, Texture gameOverBg, Texture exitButton,
                           float exitX, float exitY, float exitW, float exitH,
                           Sound gameOverSound, Music truckSound, Music introSound) {
                            
        this.viewport = viewport;
        this.batch = batch;
        this.gameOverBg = gameOverBg;
        this.exitButton = exitButton;
        this.exitX = exitX;
        this.exitY = exitY;
        this.exitW = exitW;
        this.exitH = exitH;
        this.gameOverSound = gameOverSound;
        this.truckSound = truckSound;
        this.introSound = introSound;
    }

    //Método chamado uma única vez quando a condição de derrota é atingida. Sua função é a transição de áudio: silenciar o ambiente do jogo e tocar os efeitos sonoros de derrota.
    public void triggerGameOver() {
        if (gameOver) return;
        gameOver = true;
        // parar sons de jogo
        if (truckSound != null && truckSound.isPlaying()) truckSound.stop();
        if (introSound != null && introSound.isPlaying()) introSound.stop();
        // tocar som de game over 
        if (gameOverSound != null && !soundPlayed) {
            gameOverSound.play(1.0f);
            soundPlayed = true;
        }
    }

    //Verifica se o jogador clicou no botão de sair.
    public void update(float delta) {
        if (!gameOver) return;

        if (Gdx.input.justTouched()) {
            Vector3 v = viewport.unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            float x = v.x;
            float y = v.y;
            if (x >= exitX && x <= exitX + exitW && y >= exitY && y <= exitY + exitH) {
                exitGameOver = true;
            }
        }
    }

    public void render() {
        if (!gameOver) return;
        // desenha tela de game over usando o sprite batch
        if (batch != null && viewport != null) {
            viewport.apply();
            batch.setProjectionMatrix(viewport.getCamera().combined);
            batch.begin();
            float goWidth = viewport.getWorldWidth() * 0.7f;
            float goHeight = viewport.getWorldHeight() * 0.7f;
            float goX = (viewport.getWorldWidth() - goWidth) / 2f;
            float goY = (viewport.getWorldHeight() - goHeight) / 2f;
            if (gameOverBg != null) batch.draw(gameOverBg, goX, goY, goWidth, goHeight);
            if (exitButton != null) batch.draw(exitButton, exitX, exitY, exitW, exitH);
            batch.end();
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean shouldExit() {
        return exitGameOver;
    }

    // não libera textures/sounds: Level continua responsável pelos assets
    public void dispose() {
    }
}
