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
    private Texture quitConfirmationTexture;
    private Texture confirmQuitTexture;
    private Texture cancelTexture;

    private Image backgroundImage;
    private Image playButton;
    private Image quitButton;
    private Image quitConfirmationDialog;
    private Image confirmQuitButton;
    private Image cancelButton;

    private Label levelLabel;
    private BitmapFont font;
    
    private boolean showingQuitConfirmation = false;

    private String[] levels = {"Level 1", "Level 2", "Level 3"};
    private int currentLevel = 0;

    private boolean playClicked = false;
    private boolean exitClicked = false;

    public Menu() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
        Gdx.input.setInputProcessor(stage);

        // Texturas
        backgroundTexture = new Texture("menuBackground.png");
        playTexture = new Texture("playButton.png");
        quitTexture = new Texture("quitButton.png");
        quitConfirmationTexture = new Texture("quitConfirmation.png");
        confirmQuitTexture = new Texture("quitConfirmButton.png");
        cancelTexture = new Texture("cancelButton.png");

        // Fundo
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundImage.setPosition(0, 0);
        stage.addActor(backgroundImage);

        // Diálogo de confirmação de sair do jogo 
        quitConfirmationDialog = new Image(quitConfirmationTexture);
        quitConfirmationDialog.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        quitConfirmationDialog.setPosition(0, 0);
        quitConfirmationDialog.setVisible(false);
        stage.addActor(quitConfirmationDialog);

        // Botões de confirmação 
        float buttonWidth = 200;
        float buttonHeight = 80;
        float buttonSpacing = 30; 
        float totalButtonsWidth = (buttonWidth * 2) + buttonSpacing;
        float startX = (Gdx.graphics.getWidth() - totalButtonsWidth) / 2;
        float buttonY = Gdx.graphics.getHeight() / 2.3f - buttonHeight / 2;

        cancelButton = new Image(cancelTexture);
        cancelButton.setSize(buttonWidth, buttonHeight);
        cancelButton.setPosition(startX, buttonY);
        cancelButton.setVisible(false);
        stage.addActor(cancelButton);

        confirmQuitButton = new Image(confirmQuitTexture);
        confirmQuitButton.setSize(buttonWidth, buttonHeight + 1);
        confirmQuitButton.setPosition(startX + buttonWidth + buttonSpacing, buttonY);
        confirmQuitButton.setVisible(false);
        stage.addActor(confirmQuitButton);

        // Botões
        playButton = new Image(playTexture);
        playButton.setSize(200, 60);
        playButton.setPosition(Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 - 10);
        stage.addActor(playButton);

        quitButton = new Image(quitTexture);
        quitButton.setSize(200, 60);
        quitButton.setPosition(Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 - 90);
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

            if (showingQuitConfirmation) {
                // Se estiver na tela de confirmação, só processa cliques nos botões de confirmação
                
        
                if (x >= confirmQuitButton.getX() && x <= confirmQuitButton.getX() + confirmQuitButton.getWidth() &&
                    y >= confirmQuitButton.getY() && y <= confirmQuitButton.getY() + confirmQuitButton.getHeight()) {
                    exitClicked = true;
                    return;
                }

                // Cancelar saída
                if (x >= cancelButton.getX() && x <= cancelButton.getX() + cancelButton.getWidth() &&
                    y >= cancelButton.getY() && y <= cancelButton.getY() + cancelButton.getHeight()) {
                    showingQuitConfirmation = false;
                    quitConfirmationDialog.setVisible(false);
                    confirmQuitButton.setVisible(false);
                    cancelButton.setVisible(false);

                    backgroundImage.setVisible(true);
                    playButton.setVisible(true);
                    quitButton.setVisible(true);
                    levelLabel.setVisible(true);
                    return;
                }
            } else {
                // Se estiver no menu principal, processa os cliques normais do menu
                
                if (x >= playButton.getX() && x <= playButton.getX() + playButton.getWidth() &&
                    y >= playButton.getY() && y <= playButton.getY() + playButton.getHeight()) {
                    playClicked = true;
                    return;
                }

                // Botão Sair
                if (x >= quitButton.getX() && x <= quitButton.getX() + quitButton.getWidth() &&
                    y >= quitButton.getY() && y <= quitButton.getY() + quitButton.getHeight()) {
                    showingQuitConfirmation = true;
                    backgroundImage.setVisible(false);
                    playButton.setVisible(false);
                    quitButton.setVisible(false);
                    levelLabel.setVisible(false);
                    
                    quitConfirmationDialog.setVisible(true);
                    confirmQuitButton.setVisible(true);
                    cancelButton.setVisible(true);
                    return;
                }

                // Carrossel - Setas clicáveis
                float arrowWidth = 20;
                if (x >= levelLabel.getX() && x <= levelLabel.getX() + arrowWidth) { 
                    currentLevel--;
                    if (currentLevel < 0) currentLevel = levels.length - 1;
                  
                    levelLabel.setText("< " + levels[currentLevel] + " >");
                } else if (x >= levelLabel.getX() + levelLabel.getWidth() - arrowWidth &&
                           x <= levelLabel.getX() + levelLabel.getWidth()) { // seta direita
                    currentLevel++;
                    if (currentLevel >= levels.length) currentLevel = 0;
                    
                    levelLabel.setText("< " + levels[currentLevel] + " >");
                }
            }
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
        if (level < 0 || level > levels.length) return; 
        currentLevel = level;
        levelLabel.setText("< " + levels[currentLevel] + " >");
    }

    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        playTexture.dispose();
        quitTexture.dispose();
        quitConfirmationTexture.dispose();
        confirmQuitTexture.dispose();
        cancelTexture.dispose();
        font.dispose();
    }
}