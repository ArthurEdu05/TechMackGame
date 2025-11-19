package br.techmackgame;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.FitViewport;
//Classe filha de level responsavel pelo level 1 
public class Level1 extends Level {

    public Level1(FitViewport viewport, SpriteBatch spriteBatch) {
        super(viewport, spriteBatch);
        this.bgSpeed = 2f;         // velocidade do fundo
    }

    @Override
    protected float getSpawnInterval() {
        return 4f;
    }

    //ambientação da fase 1
    @Override
    protected void setupBackground() {
        backgroundTexture = new Texture("backgroundlvl1.png");
        bgWidthUnits = 8f; 
        bgX1 = 0;
        bgX2 = bgWidthUnits;
    }

    @Override
    protected void setupObjects() {

        // energia para este nível
        float maxEnergy = 100f; 
        float baseEnergy = maxEnergy * 0.15f; // começa com 25% 
        energy = new Energy(baseEnergy, maxEnergy);

        // player (depende da energia)
        Texture playerTexture = new Texture("standingRight.png");
        player = new Player(playerTexture, 1, 1, 0.5f, 1f, viewport);
        player.setEnergy(energy);

        // caminhão
        float truckWidth = 4f;
        float truckHeight = 2f;
        float truckX = viewport.getWorldWidth() - truckWidth / 2;
        float truckY = 1f;
        truck = new Truck(truckX, truckY, truckWidth, truckHeight);

        // objetos que caem 
        objectTextures = new Array<>();
        objectTextures.add(new Texture("abajur.png"));
        objectTextures.add(new Texture("brinquedos.png"));
        objectTextures.add(new Texture("notebook.png"));
        objectTextures.add(new Texture("roupas.png"));
        objectTextures.add(new Texture("travesseiro.png"));
    }

    //história de intro
    @Override
    protected String getIntroText() {
        return "Seu caminhão de mudança está partindo, mas a porta traseira se abriu no meio da estrada! Recolha seus pertences e mantenha sua energia!";
    }

    @Override
    public Music getIntroSound() {
        return Gdx.audio.newMusic(Gdx.files.internal("Desert.mp3"));
    }

    @Override
    protected int getRequiredScore() {
        return 15;
    }
}