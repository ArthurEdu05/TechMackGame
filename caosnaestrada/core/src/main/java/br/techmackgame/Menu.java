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
import com.badlogic.gdx.audio.Sound;

public class Menu {

    private Stage stage;

    private Texture backgroundTexture;
    private Texture playTexture;
    private Texture quitTexture;
    private Texture restartTexture;
    private Texture resumeTexture;
    private Texture quitConfirmationTexture;
    private Texture confirmQuitTexture;
    private Texture cancelTexture;
    protected Texture characterTexture;

    private Image backgroundImage;
    private Image playButton;
    private Image quitButton;
    private Image restartButton;
    private Image resumeButton;
    private Image quitConfirmationDialog;
    private Image confirmQuitButton;
    private Image cancelButton;
    protected Image characterImage;

    private Label levelLabel;
    private BitmapFont font;
    private Sound clickSound;
    private boolean showingQuitConfirmation = false;

    // indica se o menu foi criado com botões extras (menu de pause)
    private boolean extraButtonsMode = false;

    private String[] levels = {"Level 1", "Level 2", "Level 3"};
    private int currentLevel = 0;

    private boolean playClicked = false;
    private boolean exitClicked = false;
    private boolean resumeClicked = false;
    private boolean restartClicked = false;
    private Texture menuTexture;
    private Image menuButton;
    private boolean goToMenuClicked = false;

    public Menu() {
        stage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));

        // texturas
        backgroundTexture = new Texture("menuBackground.png");
        playTexture = new Texture("playButton.png");
        quitTexture = new Texture("quitButton.png");
        quitConfirmationTexture = new Texture("quitConfirmation.png");
        confirmQuitTexture = new Texture("quitConfirmButton.png");
        cancelTexture = new Texture("cancelButton.png");

        // fundo
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundImage.setPosition(0, 0);
        stage.addActor(backgroundImage);

        // diálogo de confirmação de sair do jogo 
        quitConfirmationDialog = new Image(quitConfirmationTexture);
        quitConfirmationDialog.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        quitConfirmationDialog.setPosition(0, 0);
        quitConfirmationDialog.setVisible(false);
        stage.addActor(quitConfirmationDialog);

        // botões de confirmação 
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

        // botões
        playButton = new Image(playTexture);
        playButton.setSize(200, 60);
        playButton.setPosition(Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 - 10);
        stage.addActor(playButton);

        quitButton = new Image(quitTexture);
        quitButton.setSize(200, 60);
        quitButton.setPosition(Gdx.graphics.getWidth() / 2 - 100, Gdx.graphics.getHeight() / 2 - 90);
        stage.addActor(quitButton);

        // fonte
        font = new BitmapFont();
        Label.LabelStyle style = new Label.LabelStyle(font, Color.WHITE);

        clickSound = Gdx.audio.newSound(Gdx.files.internal("click.mp3"));
        // label do carrossel (fixo)
        levelLabel = new Label("< " + levels[currentLevel] + " >", style);
        levelLabel.setFontScale(1f);
        levelLabel.setPosition(Gdx.graphics.getWidth() / 2 - levelLabel.getWidth() / 2, 100);
        stage.addActor(levelLabel);
    }

    public Menu(boolean extraButtons) {
        this(); // chama o construtor original

        if (extraButtons) {
            this.extraButtonsMode = true;

            // texturas extras
            restartTexture = new Texture("restartButton.png");
            resumeTexture = new Texture("resumeButton.png");
            menuTexture = new Texture("menuButton.png");

            playButton.setVisible(false);
            levelLabel.setVisible(false);

            // reorganiza todos os botões 2x2
            setupExtraButtons();
        }
    }

    public void update() {
        if (Gdx.input.justTouched()) {
            // converte coordenadas da tela para coordenadas do stage
            Vector2 stageCoords = stage.screenToStageCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            float x = stageCoords.x;
            float y = stageCoords.y;

            if (showingQuitConfirmation) {
                // se estiver na tela de confirmação, só processa cliques nos botões de confirmação
                
        
                if (x >= confirmQuitButton.getX() && x <= confirmQuitButton.getX() + confirmQuitButton.getWidth() &&
                    y >= confirmQuitButton.getY() && y <= confirmQuitButton.getY() + confirmQuitButton.getHeight()) {
                    clickSound.play(0.7f);
                    exitClicked = true;
                    return;
                }

                // cancelar saida
                if (x >= cancelButton.getX() && x <= cancelButton.getX() + cancelButton.getWidth() &&
                    y >= cancelButton.getY() && y <= cancelButton.getY() + cancelButton.getHeight()) {
                    clickSound.play(0.7f);
                    showingQuitConfirmation = false;
                    quitConfirmationDialog.setVisible(false);
                    confirmQuitButton.setVisible(false);
                    cancelButton.setVisible(false);


                    backgroundImage.setVisible(true);
                    // somente reexibe o botão de play e o label de nível se NÃO estivermos no modo de menu de pausa (extraButtonsMode). No menu de pausa, o botão de play deve permanecer invisível.
                    if (!extraButtonsMode) {
                        playButton.setVisible(true);
                        levelLabel.setVisible(true);
                    }
                    quitButton.setVisible(true);
                    if (resumeButton != null) resumeButton.setVisible(true);
                    if (restartButton != null) restartButton.setVisible(true);
                    if (menuButton != null) menuButton.setVisible(true);

                    return;
                }
            } else {
                // se estiver no menu principal, processa os cliques normais do menu
                
                if (x >= playButton.getX() && x <= playButton.getX() + playButton.getWidth() &&
                    y >= playButton.getY() && y <= playButton.getY() + playButton.getHeight()) {
                    
                    clickSound.play(0.7f);

                    playClicked = true; // jogar no menu inicial
                    
                    return;
                }

                // sair
                if (x >= quitButton.getX() && x <= quitButton.getX() + quitButton.getWidth() &&
                    y >= quitButton.getY() && y <= quitButton.getY() + quitButton.getHeight()) {
                    clickSound.play(0.7f);
                    showingQuitConfirmation = true;
                    backgroundImage.setVisible(false);
                    playButton.setVisible(false);
                    quitButton.setVisible(false);
                    levelLabel.setVisible(false);
                    if (resumeButton != null) resumeButton.setVisible(false);
                    if (restartButton != null) restartButton.setVisible(false);
                    if (menuButton != null) menuButton.setVisible(false);

                    quitConfirmationDialog.setVisible(true);
                    confirmQuitButton.setVisible(true);
                    cancelButton.setVisible(true);

                    return;
                }

                if (resumeButton != null &&
                    x >= resumeButton.getX() && x <= resumeButton.getX() + resumeButton.getWidth() &&
                    y >= resumeButton.getY() && y <= resumeButton.getY() + resumeButton.getHeight()) {
                    clickSound.play(0.7f);
                    resumeClicked = true;
                    return;
                }

                if (restartButton != null &&
                    x >= restartButton.getX() && x <= restartButton.getX() + restartButton.getWidth() &&
                    y >= restartButton.getY() && y <= restartButton.getY() + restartButton.getHeight()) {
                    clickSound.play(0.7f);
                    restartClicked = true;
                    return;
                }

                if (menuButton != null &&
                    x >= menuButton.getX() && x <= menuButton.getX() + menuButton.getWidth() &&
                    y >= menuButton.getY() && y <= menuButton.getY() + menuButton.getHeight()) {

                    clickSound.play(0.7f);
                    goToMenuClicked = true;
                    return;
                }

                // carrossel
                float arrowWidth = 20;
                if (x >= levelLabel.getX() && x <= levelLabel.getX() + arrowWidth) { 
                    clickSound.play(0.7f);
                    currentLevel--;
                    if (currentLevel < 0) currentLevel = levels.length - 1;
                  
                    levelLabel.setText("< " + levels[currentLevel] + " >");
                } else if (x >= levelLabel.getX() + levelLabel.getWidth() - arrowWidth &&
                           x <= levelLabel.getX() + levelLabel.getWidth()) { 
                    clickSound.play(0.7f);
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

    public boolean shouldResumeGame() {
        return resumeClicked;
    }

    public boolean shouldRestartGame() {
        return restartClicked;
    }

    public boolean shouldReturnToMenu() {
        return goToMenuClicked;
    }

    public int getSelectedLevel() {
        return currentLevel;
    }
    
    public void setSelectedLevel(int level) {
        if (level < 0 || level > levels.length) return; 
        currentLevel = level;
        levelLabel.setText("< " + levels[currentLevel] + " >");
    }

    private void setupExtraButtons() {
        float buttonWidth = 200;
        float buttonHeight = 60;
        float spacingX = 50; // espaço horizontal entre colunas
        float spacingY = 20; // espaço vertical entre linhas

        // posição central da tela
        float centerX = Gdx.graphics.getWidth() / 2;
        float centerY = Gdx.graphics.getHeight() / 2;

        // primeira coluna: continuar / reiniciar
        float col1X = centerX - buttonWidth - spacingX / 2;
        float col1Y = centerY + buttonHeight / 2;

        resumeButton = new Image(resumeTexture);
        resumeButton.setSize(buttonWidth, buttonHeight);
        resumeButton.setPosition(col1X, col1Y);
        stage.addActor(resumeButton);

        restartButton = new Image(restartTexture);
        restartButton.setSize(buttonWidth, buttonHeight);
        restartButton.setPosition(col1X, col1Y - (buttonHeight + spacingY));
        stage.addActor(restartButton);

        // segunda coluna: jogar / sair
        float col2X = centerX + spacingX / 2;
        float col2Y = centerY + buttonHeight / 2;

        menuButton = new Image(menuTexture);
        menuButton.setSize(buttonWidth, buttonHeight);
        menuButton.setPosition(col2X, col2Y);
        stage.addActor(menuButton);

        quitButton.setPosition(col2X, col2Y - (buttonHeight + spacingY));

        // personagem
        characterTexture = new Texture("sitCharacter.png"); 
        characterImage = new Image(characterTexture);

        float characterWidth = 200; 
        float characterHeight = 300;

        characterImage.setSize(characterWidth, characterHeight);

        
        float characterX = col1X - characterWidth + 80;
        float characterY = col1Y - characterHeight + 20;

        characterImage.setPosition(characterX, characterY);
        stage.addActor(characterImage);
    }

    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        playTexture.dispose();
        quitTexture.dispose();
        quitConfirmationTexture.dispose();
        confirmQuitTexture.dispose();
        cancelTexture.dispose();
        if (restartTexture != null) restartTexture.dispose();
        if (resumeTexture != null) resumeTexture.dispose();
        font.dispose();
        if (clickSound != null) clickSound.dispose();
    }

    public Stage getStage() {
        return stage;
    }

    // ativa menu pra receber o input
    public void activate() {
        Gdx.input.setInputProcessor(stage);
    }
}