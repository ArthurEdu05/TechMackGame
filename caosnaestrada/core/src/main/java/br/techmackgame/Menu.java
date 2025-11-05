package br.techmackgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.viewport.FitViewport;

public class Menu {

    private Stage stage;

    private Texture backgroundTexture;
    private Texture playTexture;
    private Texture quitTexture;

    private Image backgroundImage;
    private Image playButton;
    private Image quitButton;

    private Label levelLabel;
    private BitmapFont font;

    private String[] levels = {"Level 1", "Level 2", "Level 3"};
    private int currentLevel = 0;

    private boolean playClicked = false;
    private boolean exitClicked = false;

    public Menu() {
        // Stage em pixels
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        // Texturas
        backgroundTexture = new Texture("menuBackground.png");
        playTexture = new Texture("playButton.png");
        quitTexture = new Texture("quitButton.png");

        // Fundo
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundImage.setPosition(0, 0);
        stage.addActor(backgroundImage);

        // Botões
        playButton = new Image(playTexture);
        playButton.setSize(200, 60);
        playButton.setPosition(Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 - 10);
        stage.addActor(playButton);

        quitButton = new Image(quitTexture);
        quitButton.setSize(200, 60);
        quitButton.setPosition(Gdx.graphics.getWidth() / 2 - 90, Gdx.graphics.getHeight() / 2 - 100);
        stage.addActor(quitButton);

        // Fonte
        font = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        // Label do Carrossel (fixo)
        levelLabel = new Label("< " + levels[currentLevel] + " >", style);
        levelLabel.setFontScale(1f);
        levelLabel.setPosition(Gdx.graphics.getWidth() / 2 - levelLabel.getWidth() / 2, 100);
        stage.addActor(levelLabel);
    }

    public void update() {
        if (Gdx.input.justTouched()) {
            // Converte coordenadas da tela para coordenadas do stage
            Vector2 stageCoords = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            float x = stageCoords.x;
            float y = stageCoords.y;

            // Botão Jogar
            if (x >= playButton.getX() && x <= playButton.getX() + playButton.getWidth() &&
                y >= playButton.getY() && y <= playButton.getY() + playButton.getHeight()) {
                playClicked = true;
                return;
            }

            // Botão Sair
            if (x >= quitButton.getX() && x <= quitButton.getX() + quitButton.getWidth() &&
                y >= quitButton.getY() && y <= quitButton.getY() + quitButton.getHeight()) {
                exitClicked = true;
                return;
            }

            // Carrossel - Setas clicáveis
            float arrowWidth = 20; // tamanho clicável das setas
            if (x >= levelLabel.getX() && x <= levelLabel.getX() + arrowWidth) { // seta esquerda
                currentLevel--;
                if (currentLevel < 0) currentLevel = levels.length - 1;
            } else if (x >= levelLabel.getX() + levelLabel.getWidth() - arrowWidth &&
                       x <= levelLabel.getX() + levelLabel.getWidth()) { // seta direita
                currentLevel++;
                if (currentLevel >= levels.length) currentLevel = 0;
            }

            // Atualiza texto do label sem mudar a posição
            levelLabel.setText("< " + levels[currentLevel] + " >");
        }
    }

    public void render() {
        stage.act();
        stage.draw();
    }

    public boolean shouldStartGame() {
        return playClicked;
    }

    public boolean shouldExitGame() {
        return exitClicked;
    }

    public int getSelectedLevel() {
        return currentLevel;
    }
    
    public void setSelectedLevel(int level) {
        if (level < 0 || level > levels.length) return; // Evita erro se número for inválido
        currentLevel = level;
        levelLabel.setText("< " + levels[currentLevel] + " >"); // atualiza o texto na tela
    }

    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        playTexture.dispose();
        quitTexture.dispose();
        font.dispose();
    }
}