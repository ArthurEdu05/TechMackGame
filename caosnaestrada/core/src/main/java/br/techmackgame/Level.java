
package br.techmackgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

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
     
    // elementos de UI
    protected BitmapFont font;
    protected Stage uiStage;
    protected Label energyLabel;
    protected Label scoreLabel;
    protected int score = 0; 

    // intro e contagem
    protected Label introLabel;
    protected Label countdownLabel;
    protected float introTimer = 0f;
    protected float countdownTimer = 0f;
    protected boolean showingIntro = true;
    protected boolean showingCountdown = false;
    protected boolean allowPlayerMovement = false;
    protected String introText;

    // efeitos sonoros
    protected Array<Sound> objectSounds;
    protected Music truckSound;
    protected Sound impactSound;
    protected Sound collectSound;
    protected int lastCount = -1;
    protected Sound countSound;
    protected Sound goSound;

    protected Music introSound;

    // game over

    protected Texture gameOverBg;
    protected Texture exitButton;
    protected float exitX, exitY, exitW, exitH;
    protected boolean gameOver = false;
    protected boolean exitGameOver = false;
    protected Sound gameOverSound;
    protected boolean gameOverSoundPlayed = false;
    protected GameOverManager gameOverManager;

    // pause
    protected boolean paused = false;
    protected Menu pauseMenu = null;
    protected Texture pauseButtonTexture;
    protected Image pauseButton;
    public boolean wantsRestart = false;
    public boolean wantsReturnToMenu = false;
    

    
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

        scoreLabel = new Label("Pontuação: 0", style);
        scoreLabel.setPosition(10, Gdx.graphics.getHeight() - 55); // um pouco abaixo da energia
        uiStage.addActor(scoreLabel);

        // intro texto
        introText = getIntroText();
        Label.LabelStyle introStyle = new Label.LabelStyle(font, Color.WHITE);
        introLabel = new Label(introText, introStyle);
        introLabel.setWrap(true);
        introLabel.setWidth(Gdx.graphics.getWidth() * 0.6f); // 80% da tela
        introLabel.setFontScale(1f);
        introLabel.setPosition(
            (Gdx.graphics.getWidth() - introLabel.getWidth()) / 2f,
            Gdx.graphics.getHeight() - 80
        );
        uiStage.addActor(introLabel);

        // contagem regressiva
        Label.LabelStyle countdownStyle = new Label.LabelStyle(font, Color.YELLOW);
        countdownLabel = new Label("", countdownStyle);
        countdownLabel.setFontScale(2f);
        countdownLabel.setPosition(
            (Gdx.graphics.getWidth() - countdownLabel.getWidth()) / 2f,
            Gdx.graphics.getHeight() / 2f
        );
        countdownLabel.setVisible(false);
        uiStage.addActor(countdownLabel);

        setupObjects();
        setupBackground();

        // som caminhao (loop)
        truckSound = Gdx.audio.newMusic(Gdx.files.internal("truckSound.mp3"));
        truckSound.setLooping(true);
        truckSound.setVolume(0.25f); // volume inicial

        objectSounds = new Array<>();
        objectSounds.add(Gdx.audio.newSound(Gdx.files.internal("fallingSound.mp3")));
        objectSounds.add(Gdx.audio.newSound(Gdx.files.internal("fallingSound2.mp3")));
        objectSounds.add(Gdx.audio.newSound(Gdx.files.internal("fallingSound3.mp3")));
        objectSounds.add(Gdx.audio.newSound(Gdx.files.internal("fallingSound4.mp3")));
        objectSounds.add(Gdx.audio.newSound(Gdx.files.internal("fallingSound5.mp3")));
        objectSounds.add(Gdx.audio.newSound(Gdx.files.internal("fallingSound6.mp3")));

        impactSound = Gdx.audio.newSound(Gdx.files.internal("fellSound1.mp3"));
        collectSound = Gdx.audio.newSound(Gdx.files.internal("getObject.mp3"));
        countSound = Gdx.audio.newSound(Gdx.files.internal("countSound.mp3"));
        goSound = Gdx.audio.newSound(Gdx.files.internal("goSound.mp3"));

        introSound = getIntroSound();
        introSound.setLooping(false);  // mantém tocando até o jogo começar
        introSound.setVolume(0.6f);   // volume ajustável
        introSound.play();            // toca assim que entra na intro


        if (player != null) {
            playerStartX = player.getX();
        }

       // game over UI
        gameOverBg = new Texture("GameOver.png");
        exitButton = new Texture("btnSairGameOver.png");
        gameOverSound = Gdx.audio.newSound(Gdx.files.internal("gameOver.mp3"));
        gameOverSoundPlayed = false;

        // área do botão
        exitW = 2f;
        exitH = 0.6f;
        exitX = 3f;
        exitY = 1.1f;

        // pause
        pauseButtonTexture = new Texture("pauseButton.png");
        pauseButton = new Image(pauseButtonTexture);

        // define tamanho em pixels
        pauseButton.setSize(80, 80);

        // define posição em pixels, no canto superior direito
        pauseButton.setPosition(Gdx.graphics.getWidth() - 90, Gdx.graphics.getHeight() - 90);

        // adiciona ao stage
        uiStage.addActor(pauseButton);
        // registra o stage do level como input processor por padrão
        Gdx.input.setInputProcessor(uiStage);
        // inicializa GameOverManager (não transfere propriedade das textures)
        gameOverManager = new GameOverManager(viewport, spriteBatch, gameOverBg, exitButton,
            exitX, exitY, exitW, exitH, gameOverSound, truckSound, introSound);
            
        }

    protected abstract int getRequiredScore();

    protected abstract Music getIntroSound();

    protected abstract String getIntroText();

    // cada level define seu spawn interval
    protected abstract float getSpawnInterval(); 

    protected abstract void setupBackground();
    protected abstract void setupObjects();

    public void update(float delta) {

        // pause

        // permitir abrir/fechar pause com ESC
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            if (!paused) {
                paused = true;
                allowPlayerMovement = false;
                pauseMenu = new Menu(true);
                pauseMenu.activate();
            } else if (paused && pauseMenu != null) {
                paused = false;
                allowPlayerMovement = true;
                pauseMenu.dispose();
                pauseMenu = null;
                Gdx.input.setInputProcessor(uiStage);
            }
        }

        if (!paused && Gdx.input.justTouched()) {
            Vector2 touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            touchPos = uiStage.screenToStageCoordinates(touchPos);

            if (touchPos.x >= pauseButton.getX() && touchPos.x <= pauseButton.getX() + pauseButton.getWidth() &&
                touchPos.y >= pauseButton.getY() && touchPos.y <= pauseButton.getY() + pauseButton.getHeight()) {
                paused = true;
                allowPlayerMovement = false;
                pauseMenu = new Menu(true);
                pauseMenu.activate();
            }
        }

        if (paused && pauseMenu != null) {
            pauseMenu.update();

            if (pauseMenu.shouldResumeGame()) { // clicou em continuar
                paused = false;
                allowPlayerMovement = true;
                pauseMenu.dispose();
                pauseMenu = null;
                Gdx.input.setInputProcessor(uiStage);
            } else if (pauseMenu.shouldRestartGame()) { // clicou em reiniciar
                paused = false;
                allowPlayerMovement = true;
                wantsRestart = true;
                pauseMenu.dispose();
                pauseMenu = null;
                Gdx.input.setInputProcessor(uiStage);
            } else if (pauseMenu.shouldReturnToMenu()) { // clicou em rogar
                wantsReturnToMenu = true;
            } else if (pauseMenu.shouldExitGame()) { // clicou em sair
                Gdx.app.exit();
            }

            return; // não atualiza nada do level enquanto pausado
        }

        // delega o comportamento de tela de game over ao manager
        if (gameOverManager != null && gameOverManager.isGameOver()) {
            gameOverManager.update(delta);
            if (gameOverManager.shouldExit()) exitGameOver = true;
            return;
        }

        // intro
        if (showingIntro) {
            introTimer += delta;
            if (introTimer >= 6.8f) { // mostra o texto por 6.8 segundos
                showingIntro = false;
                showingCountdown = true;
                introLabel.setVisible(false);
                countdownTimer = 3f;
                countdownLabel.setVisible(true);
            }
        }

        if (showingCountdown) {
            countdownTimer -= delta;
            int count = (int)Math.ceil(countdownTimer);

            // toca som apenas quando o número mudar
            if (count != lastCount) {
                lastCount = count;
                countdownLabel.setText(String.valueOf(count));

                if (count == 3 || count == 2 || count == 1) {
                    countSound.play(0.7f);
                } else if (count == 0) {
                    goSound.play(0.8f);
                    countdownLabel.setVisible(false);
                    showingCountdown = false;
                    allowPlayerMovement = true;
                }
            }
        }
        
        if (playerStartedMoving) {
            if (energy != null) energy.update(delta);
        }

        if (player != null) {
            if (!allowPlayerMovement) {
                player.setStanding(); // mantém parado, sem responder às teclas
            } else {
                player.update(delta);
            }
        }

        // atualiza texto da UI com porcentagem 
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
        if (playerStartedMoving) {
            spawnTimer += delta;
            if ((fallingObject == null || !fallingObject.isActive()) && spawnTimer > getSpawnInterval()) {
                spawnTimer = 0f;

                Texture randomTexture = objectTextures.random();
                float startX = truck.getX();
                float startY = truck.getY() + truck.getHeight();

                fallingObject = new FallingObject(randomTexture, startX, startY, 0.5f, 0.5f);
                // atribui pontos ao objeto já no spawn (usado tanto em coleta quanto na penalidade)
                fallingObject.setPoints(pointsForTexture(randomTexture));
                fallingObject.setImpactSound(impactSound);
                if (objectSounds != null && objectSounds.size > 0) {
                    Sound randomSound = objectSounds.random();

                    // gera volume e pitch
                    float volume = 0.3f;
                    float pitch = 0.9f + (float)Math.random() * 0.2f;

                    randomSound.play(volume, pitch, 0f);
                }
            }
        }

        if (fallingObject != null) {
            fallingObject.update(delta);
            if (fallingObject.isActive() && player.getBounds().overlaps(fallingObject.getBounds())) {
                fallingObject.collect();
                collectSound.play(0.8f); 
            }

            // caiu no chão perde pontos (penalidade = pontos do próprio objeto)
            if (fallingObject.hasHitGround()) {
               // garante que o objeto tem pontos definidos
            if (fallingObject.getPoints() <= 0) {
                fallingObject.setPoints(pointsForTexture(fallingObject.getTexture()));
            }

            // atualiza pontuação usando helper (delta negativo para penalidade)
            addScore(-fallingObject.getPoints());

                // debug: mostra valor antes e depois
            System.out.println("Pontos perdidos: " + fallingObject.getPoints() + " | Total: " + score);
                fallingObject.deactivate();
            }
        }

        if (fallingObject != null && fallingObject.isCollected()) {
            // garante que o objeto tem pontos definidos
            if (fallingObject.getPoints() <= 0) {
                fallingObject.setPoints(pointsForTexture(fallingObject.getTexture()));
            }

            // soma os pontos
            addScore(fallingObject.getPoints());

            // verifica se completou o nível
            if (score >= getRequiredScore() && !levelComplete) {
                levelComplete = true;
                truckSound.stop();
            }

            System.out.println("Pontos ganhos: " + fallingObject.getPoints() + " | Total: " + score);

            // desativa o objeto após contabilizar
            fallingObject.deactivate();
        }

        // fundo só move se player andou
        if (playerStartedMoving) {
            bgX1 -= bgSpeed * delta;
            bgX2 -= bgSpeed * delta;
            if (bgX1 + bgWidthUnits < 0) bgX1 = bgX2 + bgWidthUnits;
            if (bgX2 + bgWidthUnits < 0) bgX2 = bgX1 + bgWidthUnits;
        }

        if (playerStartedMoving && !truckSound.isPlaying()) {
            introSound.stop();
            truckSound.play();
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

        // desenha UI Stage (labels)
        if (uiStage != null) {
            // uiStage usa coordenadas de pixels; desenha por cima do spriteBatch
            spriteBatch.end();
            uiStage.getViewport().apply();
            uiStage.draw();
            spriteBatch.begin();
        }

        spriteBatch.end();

    // delega render de game over ao manager
    if (gameOverManager != null) gameOverManager.render();

        // pause

        if (paused && pauseMenu != null) {
            pauseMenu.render();
        }
    }

    // retorna quantidade de pontos
    protected int pointsForTexture(Texture texture) {
        if (texture == null) return 1;
        String s = texture.toString();
        if (s.contains("abajur")) return 1;
        if (s.contains("brinquedos")) return 2;
        if (s.contains("notebook")) return 3;
        if (s.contains("roupas")) return 4;
        if (s.contains("travesseiro")) return 5;
        return 1;
    }

    /**
     * atualiza a pontuação adicionando o delta, usa delta negativo para penalidades
     * centraliza efeitos colaterais
     */
    protected void addScore(int delta) {
        score += delta;
        if (scoreLabel != null) scoreLabel.setText("Pontuação: " + score);

        // se a pontuação ficar negativa, dispara game over via manager
        if (!gameOver && score < 0) {
            gameOver = true;
            allowPlayerMovement = false;
            if (gameOverManager != null) gameOverManager.triggerGameOver();
            System.out.println("GAME OVER!");
        }
    }

    public boolean isLevelComplete() {
        return levelComplete;
    }

    public void setLevelComplete(boolean complete) {
        this.levelComplete = complete;

        if (levelComplete && truckSound.isPlaying()) {
            truckSound.stop();
        }
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public boolean shouldExitGameOver() {
        return exitGameOver;
    }

    public void dispose() {
        for (Texture t : objectTextures) t.dispose();
        backgroundTexture.dispose();
        if (font != null) font.dispose();
        if (uiStage != null) uiStage.dispose();
        if (objectSounds != null) {
            for (Sound s : objectSounds) s.dispose();
        }
        countSound.dispose();
        goSound.dispose();
        introSound.dispose();
        truckSound.dispose();
        impactSound.dispose();

        if (collectSound != null) collectSound.dispose();

        if (gameOverBg != null) gameOverBg.dispose();
        if (exitButton != null) exitButton.dispose();

        if (gameOverSound != null) gameOverSound.dispose();
    if (gameOverManager != null) gameOverManager.dispose();
    }
}