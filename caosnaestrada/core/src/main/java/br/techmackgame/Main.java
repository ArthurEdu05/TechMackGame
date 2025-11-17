package br.techmackgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main implements ApplicationListener {

    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private Menu menu;
    private boolean gameStarted = false;
    private Level currentLevel;

    // índice do level atual
    private int currentLevelIndex = 0;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);
        menu = new Menu();

        // começa selecionando Level 1 no menu
        menu.setSelectedLevel(0);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();

        if (!gameStarted) {

            // desenha menu
            menu.update();
            ScreenUtils.clear(Color.BLACK);
            menu.render();

            // iniciou o jogo?
            if (menu.shouldStartGame()) {
                currentLevelIndex = menu.getSelectedLevel(); // salva index
                currentLevel = createLevelByIndex(currentLevelIndex);

                System.out.println("Iniciando Level " + (currentLevelIndex + 1));

                gameStarted = true;
                menu.dispose();
            }

            if (menu.shouldExitGame()) {
                Gdx.app.exit();
            }

        } else {

            if (currentLevel != null) {
                currentLevel.update(delta);
                currentLevel.render();

                // game over?
                if (currentLevel.shouldExitGameOver()) {
                    currentLevel.dispose();
                    currentLevel = null;

                    gameStarted = false;
                    menu = new Menu();
                    menu.setSelectedLevel(0);

                    return;
                }

                // terminou o level?
                if (currentLevel.isLevelComplete()) {

                    currentLevelIndex++;

    
                    if (currentLevelIndex >= 3) {

                        System.out.println("Todos os níveis completos! Voltando ao menu...");

                        // descarta o level atual
                        currentLevel.dispose();
                        currentLevel = null;

                        // volta pro menu
                        gameStarted = false;
                        menu = new Menu();
                        menu.setSelectedLevel(0);

                        return;
                    }

                    // carrega o próximo level normalmente
                    System.out.println("Indo para Level " + (currentLevelIndex + 1));
                    currentLevel.dispose();
                    currentLevel = createLevelByIndex(currentLevelIndex);
                }

            } else {
                // fallback de segurança
                System.err.println("currentLevel está nulo, retornando ao menu...");
                gameStarted = false;
                menu = new Menu();
                menu.setSelectedLevel(0);
            }
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        if (spriteBatch != null) spriteBatch.dispose();
        if (menu != null) menu.dispose();
        if (currentLevel != null) currentLevel.dispose();
    }

    // cria o level correto pelo índice
    private Level createLevelByIndex(int index) {
        switch (index) {
            case 0: return new Level1(viewport, spriteBatch);
            case 1: return new Level2(viewport, spriteBatch);
            case 2: return new Level3(viewport, spriteBatch);
            default: return new Level1(viewport, spriteBatch);
        }
    }
}