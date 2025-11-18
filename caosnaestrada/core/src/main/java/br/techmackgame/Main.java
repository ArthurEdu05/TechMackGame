package br.techmackgame;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Main implements ApplicationListener {

    private SpriteBatch spriteBatch;
    private FitViewport viewport;
    private Menu menu;
    private boolean gameStarted = false;
    private Level currentLevel;

    // win screen manager + assets
    private WinManager winManager = null;
    private Texture winBgTexture = null;
    private Texture resumeTexture = null;
    private Sound winSound = null;

    // índice do level atual
    private int currentLevelIndex = 0;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        viewport = new FitViewport(8, 5);
        menu = new Menu();
        menu.activate();

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
                    menu.activate();
                    menu.setSelectedLevel(0);

                    return;
                }

                // terminou o level? -> mostra tela de vitória e só avança quando o jogador apertar continuar
                if (currentLevel.isLevelComplete()) {

                    // se ainda não criamos o winManager, cria e dispara a tela de vitória
                    if (winManager == null) {
                        // tenta carregar assets específicos de vitória; em falha usa fallback
                        try {
                            if (winBgTexture == null) winBgTexture = new Texture("Win.png");
                        } catch (Exception e) {
                            // se não existir Win.png, winBgTexture nulo
                            winBgTexture = null;
                        }

                        try {
                            if (winSound == null) winSound = Gdx.audio.newSound(Gdx.files.internal("win.mp3"));
                        } catch (Exception e) {
                            winSound = null; // sem som se asset não existir
                        }

                        if (resumeTexture == null) resumeTexture = new Texture("resumeButton.png");

                        float resumeW = 2f;
                        float resumeH = 0.6f;
                        float resumeX = 3f;
                        float resumeY = 1.1f;

                        winManager = new WinManager(viewport, spriteBatch, winBgTexture, resumeTexture,
                            resumeX, resumeY, resumeW, resumeH, winSound, null, null);
                        winManager.triggerWin();
                    }

                    // se a tela de vitória está ativa, atualiza e renderiza ela aqui
                    if (winManager != null && winManager.isWin()) {
                        winManager.update(Gdx.graphics.getDeltaTime());
                        winManager.render();

                        if (winManager.shouldContinue()) {
                            // jogador clicou em continuar -> avança para o próximo level
                            currentLevelIndex++;

                            if (currentLevelIndex >= 3) {
                                System.out.println("Todos os níveis completos! Voltando ao menu...");

                                // descarta o level atual
                                currentLevel.dispose();
                                currentLevel = null;

                                // limpa winManager e assets
                                if (winManager != null) { winManager.dispose(); winManager = null; }
                                if (winBgTexture != null) { winBgTexture.dispose(); winBgTexture = null; }
                                if (resumeTexture != null) { resumeTexture.dispose(); resumeTexture = null; }

                                // volta pro menu
                                gameStarted = false;
                                menu = new Menu();
                                menu.activate();
                                menu.setSelectedLevel(0);

                                return;
                            }

                            // carrega o próximo level normalmente
                            System.out.println("Indo para Level " + (currentLevelIndex + 1));
                            currentLevel.dispose();
                            currentLevel = createLevelByIndex(currentLevelIndex);

                            // limpa winManager e assets
                            if (winManager != null) { winManager.dispose(); winManager = null; }
                            if (winBgTexture != null) { winBgTexture.dispose(); winBgTexture = null; }
                            if (resumeTexture != null) { resumeTexture.dispose(); resumeTexture = null; }
                        }

                        // enquanto a tela de vitória estiver ativa, não processa o restante do loop
                        return;
                    }
                }

                // pediu restart?
                if (currentLevel.wantsRestart) {

                    System.out.println("Reiniciando Level " + (currentLevelIndex + 1));

                    currentLevel.dispose();
                    currentLevel = createLevelByIndex(currentLevelIndex);

                    return;
                }

                // voltar pro menu

                if (currentLevel.wantsReturnToMenu) {
                    currentLevel.dispose();
                    currentLevel = null;

                    gameStarted = false;
                    menu = new Menu();      // volta ao menu inicial
                    menu.activate();
                    menu.setSelectedLevel(0);
                    return;
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
        if (winManager != null) winManager.dispose();
        if (winBgTexture != null) winBgTexture.dispose();
        if (resumeTexture != null) resumeTexture.dispose();
        if (winSound != null) winSound.dispose();
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